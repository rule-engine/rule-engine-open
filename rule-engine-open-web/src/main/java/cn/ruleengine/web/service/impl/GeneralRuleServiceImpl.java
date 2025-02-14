package cn.ruleengine.web.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import cn.ruleengine.common.VersionUtils;
import cn.ruleengine.common.vo.*;
import cn.ruleengine.core.condition.ConditionGroup;
import cn.ruleengine.core.rule.GeneralRule;
import cn.ruleengine.core.rule.Rule;
import cn.ruleengine.core.value.VariableType;
import cn.ruleengine.web.config.Context;
import cn.ruleengine.web.enums.DataStatus;
import cn.ruleengine.web.enums.DataType;
import cn.ruleengine.web.enums.EnableEnum;
import cn.ruleengine.web.enums.ErrorCodeEnum;
import cn.ruleengine.web.exception.ApiException;
import cn.ruleengine.web.listener.body.GeneralRuleMessageBody;
import cn.ruleengine.web.listener.event.GeneralRuleEvent;
import cn.ruleengine.web.service.*;
import cn.ruleengine.web.store.entity.*;
import cn.ruleengine.web.store.manager.*;
import cn.ruleengine.web.store.mapper.RuleEngineGeneralRuleMapper;
import cn.ruleengine.web.util.OrikaBeanMapper;
import cn.ruleengine.web.util.PageUtils;
import cn.ruleengine.web.vo.common.DownloadListResponse;
import cn.ruleengine.web.vo.common.GoBackRequest;
import cn.ruleengine.web.vo.common.HistoryListResponse;
import cn.ruleengine.web.vo.common.ViewRequest;
import cn.ruleengine.web.vo.condition.ConfigValue;
import cn.ruleengine.web.vo.rule.general.*;
import cn.ruleengine.web.vo.user.UserData;
import cn.ruleengine.web.vo.workspace.Workspace;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/24
 * @since 1.0.0
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class GeneralRuleServiceImpl implements GeneralRuleService {

    @Resource
    private RuleEngineRuleManager ruleEngineRuleManager;
    @Resource
    private RuleEngineGeneralRuleManager ruleEngineGeneralRuleManager;
    @Resource
    private RuleEngineGeneralRuleMapper ruleEngineGeneralRuleMapper;
    @Resource
    private RuleEngineGeneralRulePublishManager ruleEngineGeneralRulePublishManager;
    @Resource
    private ApplicationEventPublisher eventPublisher;
    @Resource
    private ValueResolve valueResolve;
    @Resource
    private ConditionGroupService ruleEngineConditionGroupService;
    @Resource
    private ConditionSetService conditionSetService;
    @Resource
    private RuleEngineDataPermissionManager ruleEngineDataPermissionManager;
    @Resource
    private DataReferenceService dataReferenceService;
    @Resource
    private RuleEngineDataReferenceManager ruleEngineDataReferenceManager;
    @Resource
    private UserService userService;
    @Resource
    private OperationRecordService operationRecordService;

    /**
     * 规则列表
     *
     * @param pageRequest 分页查询数据
     * @return page
     */
    @Override
    public PageResult<ListGeneralRuleResponse> list(PageRequest<ListGeneralRuleRequest> pageRequest) {
        List<PageRequest.OrderBy> orders = pageRequest.getOrders();
        PageBase page = pageRequest.getPage();
        Workspace workspace = Context.getCurrentWorkspace();

        QueryWrapper<RuleEngineGeneralRule> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(RuleEngineGeneralRule::getWorkspaceId, workspace.getId());
        PageUtils.defaultOrder(orders, wrapper);
        ListGeneralRuleRequest query = pageRequest.getQuery();
        if (Validator.isNotEmpty(query.getName())) {
            wrapper.lambda().like(RuleEngineGeneralRule::getName, query.getName());
        }
        if (Validator.isNotEmpty(query.getCode())) {
            wrapper.lambda().like(RuleEngineGeneralRule::getCode, query.getCode());
        }
        // 遗留bug修复
        if (Validator.isNotEmpty(query.getStatus())) {
            if (query.getStatus().equals(DataStatus.PRD.getStatus())) {
                wrapper.lambda().isNotNull(RuleEngineGeneralRule::getPublishVersion);
            } else {
                wrapper.lambda().eq(RuleEngineGeneralRule::getStatus, query.getStatus());
            }
        }
        Page<RuleEngineGeneralRule> rulePage = this.ruleEngineGeneralRuleManager.page(new Page<>(page.getPageIndex(), page.getPageSize()), wrapper);
        List<RuleEngineGeneralRule> records = rulePage.getRecords();
        if (CollUtil.isEmpty(records)) {
            return new PageResult<>();
        }
        Set<Integer> userIds = records.stream().map(RuleEngineGeneralRule::getCreateUserId).collect(Collectors.toSet());
        Map<Integer, RuleEngineUser> ruleEngineUserMap = this.userService.getMapByUserIds(userIds);
        List<ListGeneralRuleResponse> collect = records.stream().map(m -> {
            ListGeneralRuleResponse listRuleResponse = new ListGeneralRuleResponse();
            BeanUtil.copyProperties(m, listRuleResponse);
            Integer createUserId = m.getCreateUserId();
            RuleEngineUser orDefault = ruleEngineUserMap.getOrDefault(createUserId, new RuleEngineUser());
            listRuleResponse.setCreateUserAvatar(orDefault.getAvatar());
            listRuleResponse.setCreateUserId(createUserId);
            listRuleResponse.setCreateUserName(orDefault.getUsername());
            return listRuleResponse;
        }).collect(Collectors.toList());

        PageResult<ListGeneralRuleResponse> result = new PageResult<>();
        result.setData(new Rows<>(collect, PageUtils.getPageResponse(rulePage)));
        return result;
    }

    /**
     * 规则code是否存在
     *
     * @param code 规则code
     * @return true存在
     */
    @Override
    public Boolean ruleCodeIsExists(String code) {
        Workspace workspace = Context.getCurrentWorkspace();
        return this.ruleEngineGeneralRuleManager.lambdaQuery()
                .eq(RuleEngineGeneralRule::getWorkspaceId, workspace.getId())
                .eq(RuleEngineGeneralRule::getCode, code)
                .exists();
    }

    /**
     * 删除规则
     *
     * @param id 规则id
     * @return true
     */
    @Override
    public Boolean delete(Integer id) {
        RuleEngineGeneralRule ruleEngineGeneralRule = this.ruleEngineGeneralRuleManager.getById(id);
        if (ruleEngineGeneralRule == null) {
            return false;
        }
        // 是否有被引用次普通规则
        this.dataReferenceService.validDataReference(VariableType.GENERAL_RULE.getType(), id);
        // 如果有发布的版本
        if (Validator.isNotEmpty(ruleEngineGeneralRule.getPublishVersion())) {
            // 从引擎中移除规则
            GeneralRuleMessageBody ruleMessageBody = new GeneralRuleMessageBody();
            ruleMessageBody.setType(GeneralRuleMessageBody.Type.REMOVE);
            ruleMessageBody.setWorkspaceId(ruleEngineGeneralRule.getWorkspaceId());
            ruleMessageBody.setWorkspaceCode(ruleEngineGeneralRule.getWorkspaceCode());
            ruleMessageBody.setRuleCode(ruleEngineGeneralRule.getCode());
            this.eventPublisher.publishEvent(new GeneralRuleEvent(ruleMessageBody));
        }
        // 删除规则同时删除规则的权限信息
        this.ruleEngineDataPermissionManager.lambdaUpdate().eq(RuleEngineDataPermission::getDataType, DataType.GENERAL_RULE.getType())
                .eq(RuleEngineDataPermission::getDataId, id)
                .remove();
        this.ruleEngineRuleManager.removeById(ruleEngineGeneralRule.getRuleId());
        // 删除规则发布记录
        this.ruleEngineGeneralRulePublishManager.lambdaUpdate().eq(RuleEngineGeneralRulePublish::getGeneralRuleId, id).remove();
        // 删除规则条件组信息
        this.ruleEngineConditionGroupService.removeConditionGroupByRuleIds(Collections.singletonList(ruleEngineGeneralRule.getRuleId()));
        // 删除规则
        this.ruleEngineGeneralRuleManager.removeById(id);
        // 记录日志
        UserData currentUser = Context.getCurrentUser();
        this.operationRecordService.save(String.format("%s <a>删除</a> 了一个普通规则 <a>%s(%s)</a>",
                currentUser.getUsername(),
                ruleEngineGeneralRule.getName(),
                ruleEngineGeneralRule.getCode()),
                // ...
                id, DataType.GENERAL_RULE.getType());
        return true;
    }

    /**
     * 获取规则定义信息
     *
     * @param id 规则id
     * @return 规则定义信息
     */
    @Override
    public GeneralRuleDefinition getRuleDefinition(Integer id) {
        RuleEngineGeneralRule engineGeneralRule = this.ruleEngineGeneralRuleManager.lambdaQuery()
                .eq(RuleEngineGeneralRule::getId, id)
                .one();
        if (engineGeneralRule == null) {
            return null;
        }
        return OrikaBeanMapper.map(engineGeneralRule, GeneralRuleDefinition.class);
    }


    /**
     * 生成测试版本，更新规则数据
     *
     * @param generalRuleBody 规则配置数据
     * @return true
     */
    @Override
    public Boolean generationRelease(GeneralRuleBody generalRuleBody) {
        // 更新规则
        RuleEngineGeneralRule ruleEngineGeneralRule = this.ruleEngineGeneralRuleManager.getById(generalRuleBody.getId());
        if (ruleEngineGeneralRule == null) {
            throw new ApiException(ErrorCodeEnum.RULE9999404.getCode(), "不存在规则:{}", generalRuleBody.getId());
        }
        // 更新版本
        if (StrUtil.isBlank(ruleEngineGeneralRule.getCurrentVersion())) {
            ruleEngineGeneralRule.setCurrentVersion(VersionUtils.INIT_VERSION);
        } else {
            // 如果测试与已经发布版本一致，则需要更新一个版本号
            if (ruleEngineGeneralRule.getCurrentVersion().equals(ruleEngineGeneralRule.getPublishVersion())) {
                // 获取下一个版本
                ruleEngineGeneralRule.setCurrentVersion(VersionUtils.getNextVersion(ruleEngineGeneralRule.getCurrentVersion()));
            }
        }
        //  更新规则信息
        ruleEngineGeneralRule.setStatus(DataStatus.TEST.getStatus());
        // 更新规则状态以及版本
        this.ruleEngineGeneralRuleMapper.updateRuleById(ruleEngineGeneralRule);
        // 保存引用的基础数据
        this.dataReferenceService.saveDataReference(generalRuleBody, ruleEngineGeneralRule.getCurrentVersion());
        // 默认结果
        DefaultAction defaultAction = generalRuleBody.getDefaultAction();
        // 添加新的测试数据
        Rule rule = new Rule();
        rule.setConditionSet(this.conditionSetService.loadConditionSet(generalRuleBody.getConditionGroup()));
        // 结果
        ConfigValue action = generalRuleBody.getAction();
        rule.setActionValue(this.valueResolve.getValue(action.getType(), action.getValueType(), action.getValue()));
        GeneralRule generalRule = new GeneralRule(rule);
        generalRule.setId(generalRuleBody.getId());
        generalRule.setCode(ruleEngineGeneralRule.getCode());
        generalRule.setName(ruleEngineGeneralRule.getName());
        generalRule.setVersion(ruleEngineGeneralRule.getCurrentVersion());
        generalRule.setWorkspaceCode(ruleEngineGeneralRule.getWorkspaceCode());
        generalRule.setWorkspaceId(ruleEngineGeneralRule.getWorkspaceId());
        generalRule.setDescription(ruleEngineGeneralRule.getDescription());
        // 如果启用了默认结果
        if (EnableEnum.ENABLE.getStatus().equals(defaultAction.getEnableDefaultAction())) {
            generalRule.setDefaultActionValue(this.valueResolve.getValue(defaultAction.getType(), defaultAction.getValueType(), defaultAction.getValue()));
        }
        // 将不再判断是否存在测试，直接执行删除sql
        this.ruleEngineGeneralRulePublishManager.lambdaUpdate()
                .eq(RuleEngineGeneralRulePublish::getStatus, DataStatus.TEST.getStatus())
                .eq(RuleEngineGeneralRulePublish::getGeneralRuleId, ruleEngineGeneralRule.getId())
                .remove();
        // 生成测试规则
        RuleEngineGeneralRulePublish rulePublish = new RuleEngineGeneralRulePublish();
        rulePublish.setGeneralRuleId(generalRule.getId());
        rulePublish.setGeneralRuleCode(generalRule.getCode());
        rulePublish.setGeneralRuleName(generalRule.getName());
        rulePublish.setData(generalRule.toJson());
        rulePublish.setStatus(DataStatus.TEST.getStatus());
        rulePublish.setWorkspaceId(generalRule.getWorkspaceId());
        // add version
        rulePublish.setVersion(ruleEngineGeneralRule.getCurrentVersion());
        rulePublish.setWorkspaceCode(generalRule.getWorkspaceCode());
        // 普通规则返回值类型
        rulePublish.setValueType(action.getValueType());
        this.ruleEngineGeneralRulePublishManager.save(rulePublish);
        // 记录日志
        UserData currentUser = Context.getCurrentUser();
        this.operationRecordService.save(String.format("%s <a>生成</a> 了一个测试版本普通规则 <a>%s(%s)</a>，版本号：<a>%s</a>",
                currentUser.getUsername(),
                ruleEngineGeneralRule.getName(),
                ruleEngineGeneralRule.getCode(),
                ruleEngineGeneralRule.getCurrentVersion()),
                // ..
                ruleEngineGeneralRule.getId(), DataType.GENERAL_RULE.getType());
        return true;
    }


    /**
     * 规则发布
     *
     * @param id 规则id
     * @return true
     */
    @Override
    public Boolean publish(Integer id) {
        RuleEngineGeneralRule ruleEngineGeneralRule = this.ruleEngineGeneralRuleManager.getById(id);
        if (ruleEngineGeneralRule == null) {
            throw new ApiException(ErrorCodeEnum.RULE9999404.getCode(), "不存在规则:{}", id);
        }
        if (ruleEngineGeneralRule.getStatus().equals(DataStatus.DEV.getStatus())) {
            throw new ApiException("该规则不可执行:{}", id);
        }
        // 如果已经是发布规则了
        if (ruleEngineGeneralRule.getStatus().equals(DataStatus.PRD.getStatus())) {
            return true;
        }
        // 修改为线上
        this.ruleEngineGeneralRuleManager.lambdaUpdate()
                .set(RuleEngineGeneralRule::getStatus, DataStatus.PRD.getStatus())
                // 更新发布的版本号
                .set(RuleEngineGeneralRule::getPublishVersion, ruleEngineGeneralRule.getCurrentVersion())
                .eq(RuleEngineGeneralRule::getId, ruleEngineGeneralRule.getId())
                .update();
        /*
         * 删除原有的线上规则数据
         * 修改为，原有线上为历史版本，后期准备回退
         */
        this.ruleEngineGeneralRulePublishManager.lambdaUpdate()
                .set(RuleEngineGeneralRulePublish::getStatus, DataStatus.HISTORY.getStatus())
                .eq(RuleEngineGeneralRulePublish::getStatus, DataStatus.PRD.getStatus())
                .eq(RuleEngineGeneralRulePublish::getGeneralRuleId, ruleEngineGeneralRule.getId())
                .update();
        // 更新测试为线上
        this.ruleEngineGeneralRulePublishManager.lambdaUpdate()
                .set(RuleEngineGeneralRulePublish::getStatus, DataStatus.PRD.getStatus())
                .eq(RuleEngineGeneralRulePublish::getStatus, DataStatus.TEST.getStatus())
                .eq(RuleEngineGeneralRulePublish::getGeneralRuleId, ruleEngineGeneralRule.getId())
                .update();
        // 加载规则
        GeneralRuleMessageBody ruleMessageBody = new GeneralRuleMessageBody();
        ruleMessageBody.setType(GeneralRuleMessageBody.Type.LOAD);
        ruleMessageBody.setRuleCode(ruleEngineGeneralRule.getCode());
        ruleMessageBody.setWorkspaceId(ruleEngineGeneralRule.getWorkspaceId());
        ruleMessageBody.setWorkspaceCode(ruleEngineGeneralRule.getWorkspaceCode());
        this.eventPublisher.publishEvent(new GeneralRuleEvent(ruleMessageBody));
        // 记录日志
        UserData currentUser = Context.getCurrentUser();
        this.operationRecordService.save(String.format("%s <a>发布</a> 了一个线上版本普通规则 <a>%s(%s)</a>，版本号：<a>%s</a>",
                currentUser.getUsername(),
                ruleEngineGeneralRule.getName(),
                ruleEngineGeneralRule.getCode(),
                ruleEngineGeneralRule.getCurrentVersion()),
                // ..
                ruleEngineGeneralRule.getId(), DataType.GENERAL_RULE.getType());
        return true;
    }

    /**
     * 获取规则信息
     *
     * @param id 规则id
     * @return 规则信息
     */
    @Override
    public GetGeneralRuleResponse getRuleConfig(Integer id) {
        RuleEngineGeneralRule ruleEngineGeneralRule = this.ruleEngineGeneralRuleManager.getById(id);
        if (ruleEngineGeneralRule == null) {
            throw new ApiException(ErrorCodeEnum.RULE9999404.getCode(), "不存在规则:{}", id);
        }
        GetGeneralRuleResponse ruleResponse = new GetGeneralRuleResponse();
        ruleResponse.setCurrentVersion(ruleEngineGeneralRule.getCurrentVersion());
        ruleResponse.setPublishVersion(ruleEngineGeneralRule.getPublishVersion());
        ruleResponse.setId(ruleEngineGeneralRule.getId());
        ruleResponse.setCode(ruleEngineGeneralRule.getCode());
        ruleResponse.setName(ruleEngineGeneralRule.getName());
        ruleResponse.setDescription(ruleEngineGeneralRule.getDescription());
        ruleResponse.setConditionGroup(this.ruleEngineConditionGroupService.getConditionGroupConfig(ruleEngineGeneralRule.getRuleId()));
        // 结果
        RuleEngineRule ruleEngineRule = this.ruleEngineRuleManager.getById(ruleEngineGeneralRule.getRuleId());
        if (ruleEngineRule != null) {
            ConfigValue action = this.valueResolve.getConfigValue(ruleEngineRule.getActionValue(), ruleEngineRule.getActionType(), ruleEngineRule.getActionValueType());
            ruleResponse.setAction(action);
        }
        // 绑定的规则id
        ruleResponse.setRuleId(ruleEngineGeneralRule.getRuleId());
        // 默认结果
        ConfigValue defaultValue = this.valueResolve.getConfigValue(ruleEngineGeneralRule.getDefaultActionValue(), ruleEngineGeneralRule.getDefaultActionType(), ruleEngineGeneralRule.getDefaultActionValueType());
        DefaultAction defaultAction = new DefaultAction(defaultValue);
        defaultAction.setEnableDefaultAction(ruleEngineGeneralRule.getEnableDefaultAction());
        ruleResponse.setDefaultAction(defaultAction);
        return ruleResponse;
    }

    /**
     * 规则预览
     *
     * @param viewRequest 规则id
     * @return GetRuleResponse
     */
    @Override
    public ViewGeneralRuleResponse view(ViewRequest viewRequest) {
        Integer id = viewRequest.getId();
        RuleEngineGeneralRule ruleEngineGeneralRule = this.ruleEngineGeneralRuleManager.getById(id);
        if (ruleEngineGeneralRule == null) {
            throw new ApiException(ErrorCodeEnum.RULE9999404.getCode(), "找不到预览的规则数据:{}", id);
        }
        if (ruleEngineGeneralRule.getStatus().equals(DataStatus.PRD.getStatus()) || viewRequest.getStatus().equals(DataStatus.PRD.getStatus())) {
            RuleEngineGeneralRulePublish engineRulePublish = this.ruleEngineGeneralRulePublishManager.lambdaQuery()
                    .eq(RuleEngineGeneralRulePublish::getStatus, DataStatus.PRD.getStatus())
                    .eq(RuleEngineGeneralRulePublish::getGeneralRuleId, id)
                    .one();
            if (engineRulePublish == null) {
                throw new ApiException(ErrorCodeEnum.RULE9999404.getCode(), "找不到发布的规则:{}", id);
            }
            String data = engineRulePublish.getData();
            GeneralRule rule = GeneralRule.buildRule(data);
            return this.getRuleResponseProcess(rule);
        }
        RuleEngineGeneralRulePublish engineRulePublish = this.ruleEngineGeneralRulePublishManager.lambdaQuery()
                .eq(RuleEngineGeneralRulePublish::getStatus, DataStatus.TEST.getStatus())
                .eq(RuleEngineGeneralRulePublish::getGeneralRuleId, id)
                .one();
        if (engineRulePublish == null) {
            throw new ApiException(ErrorCodeEnum.RULE9999404.getCode(), "找不到预览的规则数据:{}", id);
        }
        String data = engineRulePublish.getData();
        GeneralRule rule = GeneralRule.buildRule(data);
        return this.getRuleResponseProcess(rule);
    }

    /**
     * 更新规则定义信息
     *
     * @param ruleDefinition 规则定义信息
     * @return 规则id
     */
    @Override
    public Boolean updateRuleDefinition(GeneralRuleDefinition ruleDefinition) {
        // 创建规则
        RuleEngineGeneralRule ruleEngineGeneralRule = this.ruleEngineGeneralRuleManager.lambdaQuery()
                .eq(RuleEngineGeneralRule::getId, ruleDefinition.getId())
                .one();
        if (ruleEngineGeneralRule == null) {
            throw new ApiException(ErrorCodeEnum.RULE9999404.getCode(), "不存在规则:{}", ruleDefinition.getId());
        } else {
            if (Objects.equals(ruleEngineGeneralRule.getStatus(), DataStatus.TEST.getStatus())) {
                // 删除原有测试规则
                this.ruleEngineGeneralRulePublishManager.lambdaUpdate()
                        .eq(RuleEngineGeneralRulePublish::getStatus, DataStatus.TEST.getStatus())
                        .eq(RuleEngineGeneralRulePublish::getGeneralRuleId, ruleEngineGeneralRule.getId())
                        .remove();
            }
        }
        ruleEngineGeneralRule.setId(ruleDefinition.getId());
        ruleEngineGeneralRule.setName(ruleDefinition.getName());
        ruleEngineGeneralRule.setDescription(ruleDefinition.getDescription());
        ruleEngineGeneralRule.setStatus(DataStatus.DEV.getStatus());
        this.ruleEngineGeneralRuleManager.updateById(ruleEngineGeneralRule);
        // 记录日志
        UserData currentUser = Context.getCurrentUser();
        this.operationRecordService.save(String.format("%s <a>更新</a> 了普通规则 <a>%s(%s)</a> 基本信息",
                currentUser.getUsername(),
                ruleEngineGeneralRule.getName(),
                ruleEngineGeneralRule.getCode()),
                // ..
                ruleEngineGeneralRule.getId(), DataType.GENERAL_RULE.getType());
        return true;
    }

    /**
     * 保存规则定义信息
     *
     * @param ruleDefinition 规则定义信息
     * @return 规则id
     */
    @Override
    public Integer saveRuleDefinition(GeneralRuleDefinition ruleDefinition) {
        // 创建规则
        RuleEngineGeneralRule ruleEngineGeneralRule = new RuleEngineGeneralRule();
        if (this.ruleCodeIsExists(ruleDefinition.getCode())) {
            throw new ApiException("规则Code：{}已经存在", ruleDefinition.getCode());
        }
        Workspace workspace = Context.getCurrentWorkspace();
        UserData userData = Context.getCurrentUser();
        ruleEngineGeneralRule.setCreateUserId(userData.getId());
        ruleEngineGeneralRule.setWorkspaceId(workspace.getId());
        ruleEngineGeneralRule.setWorkspaceCode(workspace.getCode());
        // 关联一个空白规则
        RuleEngineRule ruleEngineRule = new RuleEngineRule();
        ruleEngineRule.setName(ruleDefinition.getName());
        ruleEngineRule.setCode(ruleDefinition.getCode());
        ruleEngineRule.setDescription(ruleDefinition.getDescription());
        ruleEngineRule.setCreateUserId(userData.getId());
        ruleEngineRule.setCreateUserName(userData.getUsername());
        this.ruleEngineRuleManager.save(ruleEngineRule);
        // general rule
        ruleEngineGeneralRule.setRuleId(ruleEngineRule.getId());
        ruleEngineGeneralRule.setId(ruleDefinition.getId());
        ruleEngineGeneralRule.setName(ruleDefinition.getName());
        ruleEngineGeneralRule.setCode(ruleDefinition.getCode());
        ruleEngineGeneralRule.setDescription(ruleDefinition.getDescription());
        ruleEngineGeneralRule.setStatus(DataStatus.DEV.getStatus());
        this.ruleEngineGeneralRuleManager.save(ruleEngineGeneralRule);
        // 记录日志
        UserData currentUser = Context.getCurrentUser();
        this.operationRecordService.save(String.format("%s <a>创建</a> 了一个普通规则 <a>%s(%s)</a>",
                currentUser.getUsername(),
                ruleEngineGeneralRule.getName(), ruleEngineGeneralRule.getCode()),
                // ..
                ruleEngineGeneralRule.getId(), DataType.GENERAL_RULE.getType());
        return ruleEngineGeneralRule.getId();
    }

    /**
     * 保存默认结果
     *
     * @param saveActionRequest 保存默认结果
     * @return true
     */
    @Override
    public Boolean saveDefaultAction(SaveDefaultActionRequest saveActionRequest) {
        Integer generalRuleId = saveActionRequest.getGeneralRuleId();
        RuleEngineGeneralRule generalRule = this.ruleEngineGeneralRuleManager.getById(generalRuleId);
        if (generalRule == null) {
            throw new ApiException(ErrorCodeEnum.RULE9999404.getCode(), "不存在规则:{}", generalRuleId);
        }
        ConfigValue configValue = saveActionRequest.getConfigValue();
        generalRule.setId(generalRuleId);
        generalRule.setDefaultActionType(configValue.getType());
        generalRule.setDefaultActionValueType(configValue.getValueType());
        generalRule.setDefaultActionValue(configValue.getValue());
        this.ruleEngineGeneralRuleMapper.updateRuleById(generalRule);
        return true;
    }

    /**
     * 是否开启默认结果
     *
     * @param defaultActionSwitchRequest 是否开启默认结果
     * @return true
     */
    @Override
    public Boolean defaultActionSwitch(DefaultActionSwitchRequest defaultActionSwitchRequest) {
        return this.ruleEngineGeneralRuleManager.lambdaUpdate()
                .set(RuleEngineGeneralRule::getEnableDefaultAction, defaultActionSwitchRequest.getEnableDefaultAction())
                .eq(RuleEngineGeneralRule::getId, defaultActionSwitchRequest.getGeneralRuleId())
                .update();
    }


    /**
     * 规则下载列表
     *
     * @param pageRequest 规则id
     * @return true
     */
    @Override
    public PageResult<DownloadListResponse> downloadList(PageRequest<IdRequest> pageRequest) {
        PageBase page = pageRequest.getPage();
        IdRequest query = pageRequest.getQuery();
        return PageUtils.page(this.ruleEngineGeneralRulePublishManager, page, () -> {
            QueryWrapper<RuleEngineGeneralRulePublish> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(RuleEngineGeneralRulePublish::getGeneralRuleId, query.getId());
            PageUtils.defaultOrder(pageRequest.getOrders(), queryWrapper, RuleEngineGeneralRulePublish::getVersion);
            return queryWrapper;
        }, m -> {
            DownloadListResponse downloadListResponse = new DownloadListResponse();
            downloadListResponse.setId(m.getId());
            downloadListResponse.setDataId(m.getGeneralRuleId());
            downloadListResponse.setCode(m.getGeneralRuleCode());
            downloadListResponse.setVersion(m.getVersion());
            downloadListResponse.setStatus(m.getStatus());
            downloadListResponse.setCreateTime(m.getCreateTime());
            downloadListResponse.setName(m.getGeneralRuleName());
            return downloadListResponse;
        });
    }

    /**
     * 查询历史版本
     *
     * @param pageRequest 规则id
     * @return true
     */
    @Override
    public PageResult<HistoryListResponse> historyList(PageRequest<IdRequest> pageRequest) {
        PageBase page = pageRequest.getPage();
        IdRequest query = pageRequest.getQuery();
        return PageUtils.page(this.ruleEngineGeneralRulePublishManager, page, () -> {
            QueryWrapper<RuleEngineGeneralRulePublish> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(RuleEngineGeneralRulePublish::getGeneralRuleId, query.getId());
            queryWrapper.lambda().eq(RuleEngineGeneralRulePublish::getStatus, DataStatus.HISTORY.getStatus());
            PageUtils.defaultOrder(pageRequest.getOrders(), queryWrapper, RuleEngineGeneralRulePublish::getVersion);
            return queryWrapper;
        }, m -> {
            HistoryListResponse historyListResponse = new HistoryListResponse();
            historyListResponse.setId(m.getId());
            historyListResponse.setDataId(m.getGeneralRuleId());
            historyListResponse.setStatus(m.getStatus());
            historyListResponse.setCode(m.getGeneralRuleCode());
            historyListResponse.setVersion(m.getVersion());
            historyListResponse.setCreateTime(m.getCreateTime());
            historyListResponse.setName(m.getGeneralRuleName());
            return historyListResponse;
        });
    }

    /**
     * 规则回退
     *
     * @param goBackRequest 回退版本信息
     * @return true
     */
    @Override
    public Boolean goBack(GoBackRequest goBackRequest) {
        Integer dataId = goBackRequest.getDataId();
        String version = goBackRequest.getVersion();
        RuleEngineGeneralRulePublish generalRulePublish = ruleEngineGeneralRulePublishManager.lambdaQuery()
                .eq(RuleEngineGeneralRulePublish::getGeneralRuleId, dataId)
                .eq(RuleEngineGeneralRulePublish::getVersion, version)
                .one();
        if (generalRulePublish == null) {
            throw new ApiException("找不到可回退的版本数据");
        }
        String data = generalRulePublish.getData();
        // 校验回退的参数 变量是否还存在，如果不存在则创建
        GeneralRule generalRule = GeneralRule.buildRule(data);
        throw new ApiException("开源版本暂时不支持请关注后续进展");
    }

    /**
     * 删除历史规则
     *
     * @param id 删除历史规则
     * @return true
     */
    @Override
    public Boolean deleteHistoricalRules(Integer id) {
        RuleEngineGeneralRulePublish generalRulePublish = this.ruleEngineGeneralRulePublishManager.getById(id);
        if (generalRulePublish == null) {
            throw new ApiException("找不到此历史版本" + id);
        }
        // 删除历史版本对基础组建的依赖关系
        this.ruleEngineDataReferenceManager.lambdaUpdate()
                .eq(RuleEngineDataReference::getDataId, generalRulePublish.getGeneralRuleId())
                .eq(RuleEngineDataReference::getDataType, DataType.GENERAL_RULE.getType())
                .eq(RuleEngineDataReference::getVersion, generalRulePublish.getVersion())
                .remove();
        // 删除这个历史版本
        return this.ruleEngineGeneralRulePublishManager.removeById(id);
    }

    /**
     * 已发布规则列表
     *
     * @param publishListRequest p
     * @return r
     */
    @Override
    public PageResult<PublishListResponse> referenceableList(PageRequest<PublishListRequest> publishListRequest) {
        PageBase page = publishListRequest.getPage();
        List<PageRequest.OrderBy> orders = publishListRequest.getOrders();
        PublishListRequest query = publishListRequest.getQuery();
        Workspace workspace = Context.getCurrentWorkspace();
        return PageUtils.page(ruleEngineGeneralRulePublishManager, page, () -> {
            QueryWrapper<RuleEngineGeneralRulePublish> wrapper = new QueryWrapper<>();
            LambdaQueryWrapper<RuleEngineGeneralRulePublish> lambdaQueryWrapper = wrapper.lambda().eq(RuleEngineGeneralRulePublish::getWorkspaceId, workspace.getId());
            PageUtils.defaultOrder(orders, wrapper);
            if (CollUtil.isNotEmpty(query.getValueType())) {
                lambdaQueryWrapper.in(RuleEngineGeneralRulePublish::getValueType, query.getValueType());
            }
            if (Validator.isNotEmpty(query.getName())) {
                lambdaQueryWrapper.like(RuleEngineGeneralRulePublish::getGeneralRuleName, query.getName());
            }
            if (Validator.isNotEmpty(query.getCode())) {
                lambdaQueryWrapper.like(RuleEngineGeneralRulePublish::getGeneralRuleCode, query.getCode());
            }
            lambdaQueryWrapper.eq(RuleEngineGeneralRulePublish::getStatus, DataStatus.PRD.getStatus());
            return wrapper;
        }, m -> {
            PublishListResponse publishListResponse = new PublishListResponse();
            publishListResponse.setId(m.getGeneralRuleId());
            publishListResponse.setName(m.getGeneralRuleName());
            publishListResponse.setCode(m.getGeneralRuleCode());
            publishListResponse.setValueType(m.getValueType());
            publishListResponse.setCreateTime(m.getCreateTime());
            return publishListResponse;
        });
    }

    /**
     * 解析Rule配置信息为ViewGeneralRuleResponse
     *
     * @param generalRule Rule
     * @return ViewGeneralRuleResponse
     */
    private ViewGeneralRuleResponse getRuleResponseProcess(GeneralRule generalRule) {
        Rule rule = generalRule.getRule();
        ViewGeneralRuleResponse ruleResponse = new ViewGeneralRuleResponse();
        ruleResponse.setId(generalRule.getId());
        ruleResponse.setName(generalRule.getName());
        ruleResponse.setCode(generalRule.getCode());
        ruleResponse.setWorkspaceId(generalRule.getWorkspaceId());
        ruleResponse.setWorkspaceCode(generalRule.getWorkspaceCode());
        ruleResponse.setDescription(generalRule.getDescription());
        ruleResponse.setCurrentVersion(generalRule.getVersion());
        List<ConditionGroup> conditionGroups = rule.getConditionSet().getConditionGroups();
        ruleResponse.setConditionGroup(this.ruleEngineConditionGroupService.pressConditionGroupConfig(conditionGroups));
        ruleResponse.setAction(valueResolve.getConfigValue(rule.getActionValue()));
        DefaultAction defaultAction;
        if (generalRule.getDefaultActionValue() != null) {
            defaultAction = new DefaultAction(this.valueResolve.getConfigValue(generalRule.getDefaultActionValue()));
            defaultAction.setEnableDefaultAction(EnableEnum.ENABLE.getStatus());
        } else {
            defaultAction = new DefaultAction();
            defaultAction.setEnableDefaultAction(EnableEnum.DISABLE.getStatus());
        }
        ruleResponse.setDefaultAction(defaultAction);
        // 规则调用接口，以及规则入参
        ruleResponse.setParameters(this.dataReferenceService.getGeneralRuleParameters(generalRule.getId(), generalRule.getVersion()));
        return ruleResponse;
    }


}
