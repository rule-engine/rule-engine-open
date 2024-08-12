package cn.ruleengine.web.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.ruleengine.common.vo.PageBase;
import cn.ruleengine.common.vo.PageRequest;
import cn.ruleengine.common.vo.PageResult;
import cn.ruleengine.common.vo.Rows;
import cn.ruleengine.web.config.Context;
import cn.ruleengine.web.service.OperationRecordService;
import cn.ruleengine.web.service.UserService;
import cn.ruleengine.web.store.entity.RuleEngineOperationRecord;
import cn.ruleengine.web.store.entity.RuleEngineUser;
import cn.ruleengine.web.store.manager.RuleEngineOperationRecordManager;
import cn.ruleengine.web.util.PageUtils;
import cn.ruleengine.web.vo.operation.record.OperationRecordRequest;
import cn.ruleengine.web.vo.operation.record.OperationRecordResponse;
import cn.ruleengine.web.vo.user.UserData;
import cn.ruleengine.web.vo.workspace.Workspace;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 〈OperationRecordServiceImpl〉
 *
 * @author 丁乾文
 * @date 2021/9/9 5:04 下午
 * @since 1.0.0
 */
@Service
public class OperationRecordServiceImpl implements OperationRecordService {

    @Resource
    private RuleEngineOperationRecordManager ruleEngineOperationRecordManager;
    @Resource
    private UserService userService;

    @Async
    @Override
    public void save(String description, Integer dataId, Integer dataType) {
        UserData currentUser = Context.getCurrentUser();
        Workspace currentWorkspace = Context.getCurrentWorkspace();
        RuleEngineOperationRecord ruleEngineOperationRecord = new RuleEngineOperationRecord();
        ruleEngineOperationRecord.setOperationTime(new Date());
        ruleEngineOperationRecord.setUserId(currentUser.getId());
        ruleEngineOperationRecord.setUsername(currentUser.getUsername());
        ruleEngineOperationRecord.setWorkspaceId(currentWorkspace.getId());
        ruleEngineOperationRecord.setWorkspaceCode(currentWorkspace.getCode());
        ruleEngineOperationRecord.setDescription(description);
        ruleEngineOperationRecord.setDataType(dataType);
        ruleEngineOperationRecord.setDataId(dataId);
        this.ruleEngineOperationRecordManager.save(ruleEngineOperationRecord);
    }

    /**
     * 操作记录
     *
     * @param recordRequestPageRequest r
     * @return r
     */
    @Override
    public PageResult<OperationRecordResponse> operationRecord(PageRequest<OperationRecordRequest> recordRequestPageRequest) {
        Workspace currentWorkspace = Context.getCurrentWorkspace();
        PageBase page = recordRequestPageRequest.getPage();
        List<PageRequest.OrderBy> orders = recordRequestPageRequest.getOrders();

        QueryWrapper<RuleEngineOperationRecord> queryWrapper = new QueryWrapper<>();
        // 只查询当前工作空间的数据
        queryWrapper.lambda().eq(RuleEngineOperationRecord::getWorkspaceCode, currentWorkspace.getCode());
        PageUtils.defaultOrder(orders, queryWrapper, RuleEngineOperationRecord::getOperationTime);
        Page<RuleEngineOperationRecord> recordPage = ruleEngineOperationRecordManager.page(new Page<>(page.getPageIndex(), page.getPageSize()), queryWrapper);
        List<RuleEngineOperationRecord> records = recordPage.getRecords();
        if (CollUtil.isEmpty(records)) {
            return new PageResult<>();
        }
        Map<Integer, RuleEngineUser> ruleEngineUserMap = this.userService.getMapByUserIds(records.stream().map(RuleEngineOperationRecord::getUserId).collect(Collectors.toSet()));
        List<OperationRecordResponse> collect = records.stream().map(m -> {
            OperationRecordResponse operationRecordResponse = new OperationRecordResponse();
            BeanUtil.copyProperties(m, operationRecordResponse);
            Integer userId = m.getUserId();
            RuleEngineUser orDefault = ruleEngineUserMap.getOrDefault(userId, new RuleEngineUser());
            operationRecordResponse.setUserAvatar(orDefault.getAvatar());
            return operationRecordResponse;
        }).collect(Collectors.toList());
        PageResult<OperationRecordResponse> result = new PageResult<>();
        result.setData(new Rows<>(collect, PageUtils.getPageResponse(recordPage)));
        return result;
    }

}
