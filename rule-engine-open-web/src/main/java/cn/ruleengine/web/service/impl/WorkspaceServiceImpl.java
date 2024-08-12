package cn.ruleengine.web.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Validator;
import cn.ruleengine.common.vo.*;
import cn.ruleengine.web.config.Context;
import cn.ruleengine.web.enums.ErrorCodeEnum;
import cn.ruleengine.web.enums.UserType;
import cn.ruleengine.web.exception.ApiException;
import cn.ruleengine.web.exception.DataPermissionException;
import cn.ruleengine.web.service.GeneralRuleService;
import cn.ruleengine.web.service.WorkspaceService;
import cn.ruleengine.web.store.entity.RuleEngineGeneralRule;
import cn.ruleengine.web.store.entity.RuleEngineUser;
import cn.ruleengine.web.store.entity.RuleEngineUserWorkspace;
import cn.ruleengine.web.store.entity.RuleEngineWorkspace;
import cn.ruleengine.web.store.manager.RuleEngineGeneralRuleManager;
import cn.ruleengine.web.store.manager.RuleEngineUserManager;
import cn.ruleengine.web.store.manager.RuleEngineUserWorkspaceManager;
import cn.ruleengine.web.store.manager.RuleEngineWorkspaceManager;
import cn.ruleengine.web.store.mapper.RuleEngineWorkspaceMapper;
import cn.ruleengine.web.util.OrikaBeanMapper;
import cn.ruleengine.web.util.PageUtils;
import cn.ruleengine.web.vo.user.UserData;
import cn.ruleengine.web.vo.workspace.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.ValidationException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @date 2020/11/21
 * @since 1.0.0
 */
@Transactional(rollbackFor = Exception.class)
@Slf4j
@Service
public class WorkspaceServiceImpl implements WorkspaceService {

    @Resource
    private RuleEngineWorkspaceManager ruleEngineWorkspaceManager;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private RuleEngineWorkspaceMapper ruleEngineWorkspaceMapper;
    @Resource
    private RuleEngineUserWorkspaceManager ruleEngineUserWorkspaceManager;
    @Resource
    private RuleEngineUserManager ruleEngineUserManager;
    @Resource
    private GeneralRuleService generalRuleService;
    @Resource
    private RuleEngineGeneralRuleManager ruleEngineGeneralRuleManager;

    /**
     * 验证名称空间code是否重复
     *
     * @param code code
     * @return Boolean
     */
    @Override
    public Boolean verifyCode(String code) {
        return !this.ruleEngineWorkspaceManager.lambdaQuery().eq(RuleEngineWorkspace::getCode, code).exists();
    }

    /**
     * 用户有权限的工作空间
     *
     * @param pageRequest 模糊查询参数
     * @return list
     */
    @Override
    public PageResult<ListWorkspaceResponse> list(PageRequest<ListWorkspaceRequest> pageRequest) {
        UserData userData = Context.getCurrentUser();
        Boolean isAdmin = userData.getIsAdmin();
        ListWorkspaceRequest query = pageRequest.getQuery();
        PageBase page = pageRequest.getPage();
        PageResult<ListWorkspaceResponse> pageResult = new PageResult<>();
        // 如果是管理员，有所有工作空间权限
        if (isAdmin) {
            QueryWrapper<RuleEngineWorkspace> queryWrapper = new QueryWrapper<>();
            if (Validator.isNotEmpty(query.getCode())) {
                queryWrapper.lambda().like(RuleEngineWorkspace::getCode, query.getCode());
            }
            if (Validator.isNotEmpty(query.getName())) {
                queryWrapper.lambda().like(RuleEngineWorkspace::getName, query.getName());
            }
            PageUtils.defaultOrder(pageRequest.getOrders(), queryWrapper);
            Page<RuleEngineWorkspace> workspacePage = this.ruleEngineWorkspaceManager.page(new Page<>(page.getPageIndex(), page.getPageSize()), queryWrapper);
            long total = workspacePage.getTotal();
            List<RuleEngineWorkspace> records = workspacePage.getRecords();
            pageResult.setData(new Rows<>(this.convertRuleEngineWorkspaceList(records), new PageResponse(page.getPageIndex(), page.getPageSize(), total)));
            return pageResult;
        }
        // 普通成员，或者空间管理员
        Integer userId = userData.getId();
        Integer total = this.ruleEngineWorkspaceMapper.totalWorkspace(userId, query, page);
        if (total == null || total == 0) {
            return new PageResult<>();
        }
        List<RuleEngineWorkspace> ruleEngineWorkspaces = this.ruleEngineWorkspaceMapper.listWorkspace(userId, query, page);
        pageResult.setData(new Rows<>(this.convertRuleEngineWorkspaceList(ruleEngineWorkspaces), new PageResponse(page.getPageIndex(), page.getPageSize(), total)));
        return pageResult;
    }

    /**
     * convertRuleEngineWorkspaceList
     *
     * @param ruleEngineWorkspaces ruleEngineWorkspaces
     * @return r
     */
    public List<ListWorkspaceResponse> convertRuleEngineWorkspaceList(List<RuleEngineWorkspace> ruleEngineWorkspaces) {
        if (CollUtil.isEmpty(ruleEngineWorkspaces)) {
            return Collections.emptyList();
        }
        List<ListWorkspaceResponse> listWorkspaceResponses = OrikaBeanMapper.mapList(ruleEngineWorkspaces, ListWorkspaceResponse.class);
        List<Integer> workspaceIds = listWorkspaceResponses.stream().map(Workspace::getId).collect(Collectors.toList());
        if (CollUtil.isEmpty(workspaceIds)) {
            return listWorkspaceResponses;
        }
        List<RuleEngineUserWorkspace> workspaceList = ruleEngineUserWorkspaceManager.lambdaQuery().eq(RuleEngineUserWorkspace::getIsAdministration, UserType.WORKSPACE_ADMINISTRATOR.getType())
                .in(RuleEngineUserWorkspace::getWorkspaceId, workspaceIds).list();
        if (CollUtil.isEmpty(workspaceList)) {
            return listWorkspaceResponses;
        }
        // 工作空间管理映射表
        Map<Integer, List<RuleEngineUserWorkspace>> workspaceAdminMap = workspaceList.stream().collect(Collectors.groupingBy(RuleEngineUserWorkspace::getWorkspaceId));
        // 用户映射表
        Set<Integer> adminUserIds = workspaceList.stream().map(RuleEngineUserWorkspace::getUserId).collect(Collectors.toSet());
        Map<Integer, RuleEngineUser> userMap = ruleEngineUserManager.lambdaQuery().in(RuleEngineUser::getId, adminUserIds).list().stream().collect(Collectors.toMap(RuleEngineUser::getId, Function.identity()));
        for (ListWorkspaceResponse listWorkspaceResponse : listWorkspaceResponses) {
            // bug修复，当没有管理时
            List<RuleEngineUserWorkspace> workspaceAdmins = workspaceAdminMap.getOrDefault(listWorkspaceResponse.getId(), Collections.emptyList());
            listWorkspaceResponse.setWorkspaceAdminList(workspaceAdmins.stream().map(m -> {
                Integer userId = m.getUserId();
                RuleEngineUser ruleEngineUser = userMap.getOrDefault(userId, new RuleEngineUser());
                ListWorkspaceResponse.AdminUser userResponse = new ListWorkspaceResponse.AdminUser();
                userResponse.setId(ruleEngineUser.getId());
                userResponse.setUsername(ruleEngineUser.getUsername());
                userResponse.setAvatar(ruleEngineUser.getAvatar());
                return userResponse;
            }).collect(Collectors.toList()));
        }
        return listWorkspaceResponses;
    }


    /**
     * 普通用户是否有这个工作空间权限
     *
     * @param workspaceId 工作空间id
     * @param userId      用户id
     * @return true有权限
     */
    @Override
    public boolean hasWorkspacePermission(Integer workspaceId, Integer userId) {
        return this.ruleEngineUserWorkspaceManager.lambdaQuery().eq(RuleEngineUserWorkspace::getWorkspaceId, workspaceId)
                .eq(RuleEngineUserWorkspace::getUserId, userId)
                .exists();
    }

    /**
     * 获取当前工作空间
     *
     * @return Workspace
     */
    @Override
    public Workspace currentWorkspace() {
        UserData userData = Context.getCurrentUser();
        RBucket<Workspace> bucket = this.redissonClient.getBucket(CURRENT_WORKSPACE + userData.getId());
        Workspace workspace = bucket.get();
        if (workspace == null) {
            RuleEngineWorkspace ruleEngineWorkspace = this.ruleEngineWorkspaceMapper.getFirstWorkspace();
            if (ruleEngineWorkspace != null) {
                workspace = new Workspace();
                workspace.setId(ruleEngineWorkspace.getId());
                workspace.setName(ruleEngineWorkspace.getName());
                workspace.setCode(ruleEngineWorkspace.getCode());
                bucket.set(workspace);
            } else {
                throw new ApiException("没有可用工作空间");
            }
        }
        log.info("当前工作空间：" + workspace);
        return workspace;
    }

    /**
     * 获取当前工作空间详情
     *
     * @return Workspace
     */
    @Override
    public CurrentWorkspaceInfoResponse currentWorkspaceInfo() {
        UserData userData = Context.getCurrentUser();
        Workspace workspace = this.currentWorkspace();
        CurrentWorkspaceInfoResponse workspaceInfoResponse = new CurrentWorkspaceInfoResponse();
        workspaceInfoResponse.setId(workspace.getId());
        workspaceInfoResponse.setName(workspace.getName());
        workspaceInfoResponse.setCode(workspace.getCode());
        if (userData.getIsAdmin()) {
            workspaceInfoResponse.setIsAdmin(true);
        } else {
            workspaceInfoResponse.setIsAdmin(this.isWorkspaceAdministrator(userData.getId(), workspace.getId()));
        }
        return workspaceInfoResponse;
    }

    /**
     * 是否为此工作空间管理员
     *
     * @param userId      用户id
     * @param workspaceId 工作空间id
     * @return true 是
     */
    @Override
    public boolean isWorkspaceAdministrator(Integer userId, Integer workspaceId) {
        RuleEngineUserWorkspace userWorkspace = this.ruleEngineUserWorkspaceManager.lambdaQuery()
                .eq(RuleEngineUserWorkspace::getUserId, userId)
                .eq(RuleEngineUserWorkspace::getWorkspaceId, workspaceId)
                .one();
        // 如果当前用户没有这个空间配置的权限
        if (userWorkspace == null) {
            throw new DataPermissionException("你没有此工作空间的操作权限");
        }
        // 如果是工作空间管理员
        return userWorkspace.getIsAdministration().equals(UserType.WORKSPACE_ADMINISTRATOR.getType());
    }

    /**
     * 切换工作空间
     *
     * @param id 工作空间id
     * @return true
     */
    @Override
    public Boolean change(Integer id) {
        RuleEngineWorkspace engineWorkspace = this.ruleEngineWorkspaceManager.getById(id);
        if (engineWorkspace == null) {
            throw new ApiException(ErrorCodeEnum.RULE9999404.getCode(), "找不到此工作空间：" + id);
        }
        UserData userData = Context.getCurrentUser();
        boolean isWorkspaceAdministrator = true;
        if (!userData.getIsAdmin()) {
            // 如果不是超级管理员，查看是否有此工作空间的工作空间权限
            if (!this.hasWorkspacePermission(id, userData.getId())) {
                throw new ApiException("你没有此工作空间权限");
            }
            isWorkspaceAdministrator = this.isWorkspaceAdministrator(userData.getId(), id);
        }
        RBucket<Workspace> bucket = this.redissonClient.getBucket(CURRENT_WORKSPACE + userData.getId());
        Workspace workspace = new Workspace();
        workspace.setId(engineWorkspace.getId());
        workspace.setName(engineWorkspace.getName());
        workspace.setCode(engineWorkspace.getCode());
        workspace.setAdministration(isWorkspaceAdministrator);
        bucket.set(workspace);
        return true;
    }


    /**
     * 根据工作空间code获取AccessKey
     *
     * @param code 工作空间code
     * @return AccessKey
     */
    @Override
    public AccessKey accessKey(String code) {
        RuleEngineWorkspace engineWorkspace = this.ruleEngineWorkspaceManager.lambdaQuery()
                .eq(RuleEngineWorkspace::getCode, code)
                .one();
        if (engineWorkspace == null) {
            throw new ApiException(ErrorCodeEnum.RULE9999404.getCode(), "找不到此工作空间：" + code);
        }
        AccessKey accessKey = new AccessKey();
        accessKey.setId(engineWorkspace.getId());
        accessKey.setAccessKeyId(engineWorkspace.getAccessKeyId());
        accessKey.setAccessKeySecret(engineWorkspace.getAccessKeySecret());
        return accessKey;
    }

    /**
     * 添加工作空间
     *
     * @param addWorkspaceRequest a
     * @return r
     */
    @Override
    public Boolean add(AddWorkspaceRequest addWorkspaceRequest) {
        if (!this.verifyCode(addWorkspaceRequest.getCode())) {
            throw new ValidationException("工作空间编码已经存在!");
        }
        RuleEngineWorkspace ruleEngineWorkspace = new RuleEngineWorkspace();
        ruleEngineWorkspace.setCode(addWorkspaceRequest.getCode());
        ruleEngineWorkspace.setName(addWorkspaceRequest.getName());
        ruleEngineWorkspace.setAccessKeyId(ACCESS_KEY_ID);
        ruleEngineWorkspace.setAccessKeySecret(ACCESS_KEY_SECRET);
        ruleEngineWorkspace.setDescription(ruleEngineWorkspace.getDescription());
        this.ruleEngineWorkspaceManager.save(ruleEngineWorkspace);
        return true;
    }

    /**
     * 编辑工作空间
     *
     * @param editWorkspaceRequest e
     * @return r
     */
    @Override
    public Boolean edit(EditWorkspaceRequest editWorkspaceRequest) {
        RuleEngineWorkspace ruleEngineWorkspace = new RuleEngineWorkspace();
        ruleEngineWorkspace.setId(editWorkspaceRequest.getId());
        ruleEngineWorkspace.setName(editWorkspaceRequest.getName());
        ruleEngineWorkspace.setDescription(editWorkspaceRequest.getDescription());
        return this.ruleEngineWorkspaceManager.updateById(ruleEngineWorkspace);
    }

    /**
     * 删除工作空间
     *
     * @param id id
     * @return r
     */
    @Override
    public Boolean delete(Integer id) {
        // 删除工作空间
        this.ruleEngineWorkspaceMapper.deleteById(id);
        // 删除工作空间人员
        this.ruleEngineUserWorkspaceManager.lambdaUpdate().eq(RuleEngineUserWorkspace::getWorkspaceId, id).remove();
        // 异步删除工作空间下的数据
        ((WorkspaceServiceImpl) AopContext.currentProxy()).syncDeleteWorkspaceData(id);
        return true;
    }

    /**
     * 更新工作空间AccessKey
     *
     * @param param p
     * @return true
     */
    @Override
    public Boolean updateAccessKey(UpdateAccessKeyRequest param) {
        RuleEngineWorkspace ruleEngineWorkspace = new RuleEngineWorkspace();
        ruleEngineWorkspace.setId(param.getId());
        ruleEngineWorkspace.setAccessKeyId(param.getAccessKeyId());
        ruleEngineWorkspace.setAccessKeySecret(param.getAccessKeySecret());
        return this.ruleEngineWorkspaceManager.updateById(ruleEngineWorkspace);
    }

    /**
     * 通过工作空间id查询工作空间信息
     *
     * @param idRequest id
     * @return r
     */
    @Override
    public SelectWorkspaceResponse selectWorkSpaceById(IdRequest idRequest) {
        RuleEngineWorkspace engineWorkspace = this.ruleEngineWorkspaceManager.getById(idRequest.getId());
        if (engineWorkspace == null) {
            throw new ApiException(ErrorCodeEnum.RULE9999404.getCode(), "找不到此工作空间：" + idRequest.getId());
        }
        SelectWorkspaceResponse selectWorkspaceResponse = new SelectWorkspaceResponse();
        BeanUtils.copyProperties(engineWorkspace, selectWorkspaceResponse);
        return selectWorkspaceResponse;
    }

    /**
     * 获取一个有权限的工作空间
     *
     * @return RuleEngineWorkspace
     */
    @Override
    public Workspace getFirstWorkspace(Integer userId, boolean isAdmin) {
        RuleEngineWorkspace ruleEngineWorkspace;
        if (isAdmin) {
            ruleEngineWorkspace = this.ruleEngineWorkspaceMapper.getFirstWorkspace();
        } else {
            // 获取用户有权限的工作空间
            ruleEngineWorkspace = this.ruleEngineWorkspaceMapper.getFirstHasPermissionWorkspace(userId);
        }
        if (ruleEngineWorkspace == null) {
            // 没有可用工作空间
            throw new ApiException(ErrorCodeEnum.RULE_4010);
        }
        Workspace workspace = new Workspace();
        workspace.setId(ruleEngineWorkspace.getId());
        workspace.setName(ruleEngineWorkspace.getName());
        workspace.setCode(ruleEngineWorkspace.getCode());
        return workspace;
    }

    /**
     * 异步删除工作空间下的数据
     *
     * @param id 工作空间id
     */
    @Async
    @Transactional(rollbackFor = Exception.class)
    public void syncDeleteWorkspaceData(Integer id) {
        // 普通规则
        log.info("开始删除工作空间下普通规则数据！");
        List<RuleEngineGeneralRule> ruleEngineGeneralRules = this.ruleEngineGeneralRuleManager.lambdaQuery()
                .eq(RuleEngineGeneralRule::getWorkspaceId, id)
                .list();
        for (RuleEngineGeneralRule ruleEngineGeneralRule : ruleEngineGeneralRules) {
            this.generalRuleService.delete(ruleEngineGeneralRule.getId());
        }
        log.info("数据删除完毕！");
    }

}
