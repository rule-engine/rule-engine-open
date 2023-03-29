package cn.ruleengine.web.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.ruleengine.web.exception.ApiException;
import cn.ruleengine.web.service.ConditionGroupConditionService;
import cn.ruleengine.web.service.ConditionService;
import cn.ruleengine.web.store.entity.RuleEngineConditionGroupCondition;
import cn.ruleengine.web.store.manager.RuleEngineConditionGroupConditionManager;
import cn.ruleengine.web.store.manager.RuleEngineConditionManager;
import cn.ruleengine.web.store.mapper.RuleEngineConditionGroupConditionMapper;
import cn.ruleengine.web.vo.common.RearrangeRequest;
import cn.ruleengine.web.vo.condition.AddConditionRequest;
import cn.ruleengine.web.vo.condition.SwitchConditionOrderRequest;
import cn.ruleengine.web.vo.condition.group.condition.DeleteConditionAndBindGroupRefRequest;
import cn.ruleengine.web.vo.condition.group.condition.SaveConditionAndBindGroupRequest;
import cn.ruleengine.web.vo.condition.group.condition.SaveConditionAndBindGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 〈ConditionGroupConditionServiceImpl〉
 *
 * @author 丁乾文
 * @date 2021/7/12 1:47 下午
 * @since 1.0.0
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class ConditionGroupConditionServiceImpl implements ConditionGroupConditionService {


    @Resource
    private ConditionService conditionService;
    @Resource
    private RuleEngineConditionManager ruleEngineConditionManager;
    @Resource
    private RuleEngineConditionGroupConditionManager ruleEngineConditionGroupConditionManager;
    @Resource
    private RuleEngineConditionGroupConditionMapper ruleEngineConditionGroupConditionMapper;

    /**
     * 保存条件并绑定到规则条件组中
     *
     * @param saveConditionAndBindGroupRequest 条件信息
     * @return 条件id
     */
    @Override
    public SaveConditionAndBindGroupResponse saveConditionAndBindGroup(SaveConditionAndBindGroupRequest saveConditionAndBindGroupRequest) {
        AddConditionRequest addConditionRequest = saveConditionAndBindGroupRequest.getAddConditionRequest();
        Integer conditionId = this.conditionService.save(addConditionRequest);
        // 中间表插入一个关系数据
        RuleEngineConditionGroupCondition ruleEngineConditionGroupCondition = new RuleEngineConditionGroupCondition();
        ruleEngineConditionGroupCondition.setConditionId(conditionId);
        ruleEngineConditionGroupCondition.setConditionGroupId(saveConditionAndBindGroupRequest.getConditionGroupId());
        ruleEngineConditionGroupCondition.setOrderNo(saveConditionAndBindGroupRequest.getOrderNo());
        this.ruleEngineConditionGroupConditionManager.save(ruleEngineConditionGroupCondition);
        // 条件组条件id
        SaveConditionAndBindGroupResponse saveConditionAndBindGroupResponse = new SaveConditionAndBindGroupResponse();
        saveConditionAndBindGroupResponse.setConditionId(conditionId);
        saveConditionAndBindGroupResponse.setConditionGroupConditionId(ruleEngineConditionGroupCondition.getId());
        return saveConditionAndBindGroupResponse;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteCondition(DeleteConditionAndBindGroupRefRequest deleteConditionAndBindGroupRefRequest) {
        // 解决有规则/规则集在引用此条件，无法删除
        this.ruleEngineConditionManager.removeById(deleteConditionAndBindGroupRefRequest.getConditionId());
        this.ruleEngineConditionGroupConditionManager.removeById(deleteConditionAndBindGroupRefRequest.getConditionGroupRefId());
        return true;
    }

    /**
     * 交换条件顺序
     *
     * @param switchOrderRequest fromId toId
     * @return boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean switchOrder(SwitchConditionOrderRequest switchOrderRequest) {
        Integer fromConditionGroupId = switchOrderRequest.getFromConditionGroupId();
        Integer toConditionGroupId = switchOrderRequest.getToConditionGroupId();
        RuleEngineConditionGroupCondition from = this.ruleEngineConditionGroupConditionManager
                .lambdaQuery()
                .eq(RuleEngineConditionGroupCondition::getConditionId, switchOrderRequest.getFromId())
                .one();
        if (Objects.isNull(from)) {
            throw new ApiException("不存在的条件:{}", switchOrderRequest.getFromId());
        }
        Integer toId = switchOrderRequest.getToId();
        RuleEngineConditionGroupCondition to = null;
        if (toId != null) {
            // 目标条件组没有任何条件时
            to = this.ruleEngineConditionGroupConditionManager
                    .lambdaQuery()
                    .eq(RuleEngineConditionGroupCondition::getConditionId, switchOrderRequest.getToId())
                    .one();
        }
        // 跨条件组拖拽
        if (!Objects.equals(fromConditionGroupId, toConditionGroupId)) {
            // 切换条件组
            from.setConditionGroupId(toConditionGroupId);
            // 目标条件组没有任何条件时  ,直接修改 from的条件组为目标条件组即可 否则执行if
            if (!Objects.isNull(to)) {
                Integer toOrderNo = to.getOrderNo();
                // 设置拖拽到目标条件组后的具体位置
                from.setOrderNo(toOrderNo);
                Integer toType = switchOrderRequest.getToType();
                if (Objects.equals(SwitchConditionOrderRequest.TOP, toType)) {
                    // toOrderNo 大于它的统统加1    from = toOrderNo 替换位置
                    this.ruleEngineConditionGroupConditionMapper.updateConditionOrderGreaterThanPlus(toConditionGroupId, toOrderNo);
                } else {
                    // 下 toOrderNo 小于它的统统减1    from = toOrderNo 替换位置
                    this.ruleEngineConditionGroupConditionMapper.updateConditionOrderLessThanMinus(toConditionGroupId, toOrderNo);
                }
            }
            this.ruleEngineConditionGroupConditionManager.updateById(from);
        } else {
            if (Objects.isNull(to)) {
                throw new ApiException("不存在的条件:{}", switchOrderRequest.getToId());
            }
            // 条件内之间互换
            Integer fromOrderNo = from.getOrderNo();
            Integer toOrderNo = to.getOrderNo();
            //交换位置
            from.setOrderNo(toOrderNo);
            to.setOrderNo(fromOrderNo);
            //跨多条数据交换
            if (!(fromOrderNo - toOrderNo == 1 || toOrderNo - fromOrderNo == 1)) {
                if (fromOrderNo > toOrderNo) { //从下往上拖拽
                    // toOrderNo 大于它的统统加1    from = toOrderNo 替换位置
                    this.ruleEngineConditionGroupConditionMapper.updateConditionOrderGreaterThanPlus(fromOrderNo, fromOrderNo);
                } else {
                    // 下 toOrderNo 小于它的统统减1    from = toOrderNo 替换位置
                    this.ruleEngineConditionGroupConditionMapper.updateConditionOrderLessThanMinus(toConditionGroupId, toOrderNo);
                }
            }
            this.ruleEngineConditionGroupConditionManager.updateBatchById(Arrays.asList(from, to));

        }
        return true;
    }

    /**
     * 重新排序
     *
     * @param rearrangeRequests r
     * @return true
     */
    @Override
    public Boolean rearrange(List<RearrangeRequest> rearrangeRequests) {
        if (CollUtil.isEmpty(rearrangeRequests)) {
            return true;
        }
        List<RuleEngineConditionGroupCondition> collect = rearrangeRequests.stream().map(m -> {
            RuleEngineConditionGroupCondition ruleEngineConditionGroupCondition = new RuleEngineConditionGroupCondition();
            ruleEngineConditionGroupCondition.setId(m.getId());
            ruleEngineConditionGroupCondition.setOrderNo(m.getOrderNo());
            return ruleEngineConditionGroupCondition;
        }).collect(Collectors.toList());
        this.ruleEngineConditionGroupConditionManager.updateBatchById(collect);
        return true;
    }

}
