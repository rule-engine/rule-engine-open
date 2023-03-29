package cn.ruleengine.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.ruleengine.core.condition.Condition;
import cn.ruleengine.core.condition.ConditionGroup;
import cn.ruleengine.core.condition.ConditionSet;
import cn.ruleengine.core.condition.Operator;
import cn.ruleengine.web.service.ConditionSetService;
import cn.ruleengine.web.service.ValueResolve;
import cn.ruleengine.web.store.entity.RuleEngineCondition;
import cn.ruleengine.web.store.entity.RuleEngineConditionGroup;
import cn.ruleengine.web.store.entity.RuleEngineConditionGroupCondition;
import cn.ruleengine.web.store.manager.RuleEngineConditionGroupConditionManager;
import cn.ruleengine.web.store.manager.RuleEngineConditionGroupManager;
import cn.ruleengine.web.store.manager.RuleEngineConditionManager;
import cn.ruleengine.web.vo.condition.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 〈ConditionSetService〉
 *
 * @author 丁乾文
 * @date 2021/6/18 11:56 上午
 * @since 1.0.0
 */
@Service
public class ConditionSetServiceImpl implements ConditionSetService {

    @Resource
    private RuleEngineConditionGroupManager ruleEngineConditionGroupManager;
    @Resource
    private RuleEngineConditionGroupConditionManager ruleEngineConditionGroupConditionManager;
    @Resource
    private RuleEngineConditionManager ruleEngineConditionManager;
    @Resource
    private ValueResolve valueResolve;


    /**
     * 获取规则配置条件集，懒得写的，待优化
     *
     * @param conditionGroup 条件组配置
     * @return 条件集
     */
    @Override
    public ConditionSet loadConditionSet(List<ConditionGroupConfig> conditionGroup) {
        ConditionSet conditionSet = new ConditionSet();
        for (ConditionGroupConfig conditionGroupConfig : conditionGroup) {
            ConditionGroup conditionGroups = new ConditionGroup();
            conditionGroups.setId(conditionGroupConfig.getId());
            conditionGroups.setName(conditionGroupConfig.getName());
            conditionGroups.setOrderNo(conditionGroupConfig.getOrderNo());
            List<ConditionGroupCondition> conditionGroupCondition = conditionGroupConfig.getConditionGroupCondition();
            if (CollUtil.isNotEmpty(conditionGroupCondition)) {
                for (ConditionGroupCondition groupCondition : conditionGroupCondition) {
                    ConditionBody conditionBody = groupCondition.getCondition();
                    Condition condition = new Condition();
                    condition.setId(conditionBody.getId());
                    condition.setName(conditionBody.getName());
                    condition.setOrderNo(groupCondition.getOrderNo());
                    ConfigBean config = conditionBody.getConfig();
                    ConfigValue leftValue = config.getLeftValue();
                    condition.setLeftValue(this.valueResolve.getValue(leftValue.getType(), leftValue.getValueType(), leftValue.getValue()));
                    condition.setOperator(Operator.getByName(config.getSymbol()));
                    ConfigValue rightValue = config.getRightValue();
                    condition.setRightValue(this.valueResolve.getValue(rightValue.getType(), rightValue.getValueType(), rightValue.getValue()));
                    conditionGroups.addCondition(condition);
                }
            }
            // 条件组内没有条件，生成测试json 则不再处理
            if (CollUtil.isNotEmpty(conditionGroups.getConditions())) {
                conditionSet.addConditionGroup(conditionGroups);
            }
        }
        return conditionSet;
    }

    /**
     * 获取规则配置条件集，懒得写的，待优化
     *
     * @param ruleId 规则id
     * @return 条件集
     */
    @Override
    public ConditionSet loadConditionSet(Integer ruleId) {
        List<RuleEngineConditionGroup> conditionGroups = this.ruleEngineConditionGroupManager.lambdaQuery()
                .eq(RuleEngineConditionGroup::getRuleId, ruleId)
                .orderByAsc(RuleEngineConditionGroup::getOrderNo)
                .list();
        if (CollUtil.isEmpty(conditionGroups)) {
            return new ConditionSet();
        }
        // 加载所有的用到的条件组条件
        Set<Integer> conditionGroupIds = conditionGroups.stream().map(RuleEngineConditionGroup::getId).collect(Collectors.toSet());
        Map<Integer, List<RuleEngineConditionGroupCondition>> conditionGroupConditionMaps = this.ruleEngineConditionGroupConditionManager.lambdaQuery()
                .in(RuleEngineConditionGroupCondition::getConditionGroupId, conditionGroupIds)
                .orderByAsc(RuleEngineConditionGroupCondition::getOrderNo)
                .list()
                .stream().collect(Collectors.groupingBy(RuleEngineConditionGroupCondition::getConditionGroupId));
        Set<Integer> conditionIds = conditionGroupConditionMaps.values().stream().flatMap(Collection::stream).map(RuleEngineConditionGroupCondition::getConditionId).collect(Collectors.toSet());
        if (CollUtil.isEmpty(conditionIds)) {
            return new ConditionSet();
        }
        List<RuleEngineCondition> ruleEngineConditions = ruleEngineConditionManager.lambdaQuery().in(RuleEngineCondition::getId, conditionIds).list();
        Map<Integer, RuleEngineCondition> conditionMap = ruleEngineConditions.stream().collect(Collectors.toMap(RuleEngineCondition::getId, Function.identity()));
        ConditionSet conditionSet = new ConditionSet();
        for (RuleEngineConditionGroup group : conditionGroups) {
            ConditionGroup conditionGroup = new ConditionGroup();
            conditionGroup.setId(group.getId());
            conditionGroup.setOrderNo(group.getOrderNo());
            List<RuleEngineConditionGroupCondition> groupConditions = conditionGroupConditionMaps.get(group.getId());
            if (CollUtil.isNotEmpty(groupConditions)) {
                for (RuleEngineConditionGroupCondition groupCondition : groupConditions) {
                    RuleEngineCondition engineCondition = conditionMap.get(groupCondition.getConditionId());
                    Condition condition = new Condition();
                    condition.setId(engineCondition.getId());
                    condition.setName(engineCondition.getName());
                    condition.setOrderNo(groupCondition.getOrderNo());
                    condition.setLeftValue(valueResolve.getValue(engineCondition.getLeftType(), engineCondition.getLeftValueType(), engineCondition.getLeftValue()));
                    condition.setOperator(Operator.getByName(engineCondition.getSymbol()));
                    condition.setRightValue(valueResolve.getValue(engineCondition.getRightType(), engineCondition.getRightValueType(), engineCondition.getRightValue()));
                    conditionGroup.addCondition(condition);
                }
                conditionSet.addConditionGroup(conditionGroup);
            }
        }
        return conditionSet;
    }


}
