package cn.ruleengine.web.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.ruleengine.core.Parameter;
import cn.ruleengine.core.exception.ValidException;
import cn.ruleengine.core.value.Formula;
import cn.ruleengine.core.value.VariableType;
import cn.ruleengine.web.config.Context;
import cn.ruleengine.web.enums.DataStatus;
import cn.ruleengine.web.enums.DataType;
import cn.ruleengine.web.service.DataReferenceService;
import cn.ruleengine.web.store.entity.*;
import cn.ruleengine.web.store.manager.*;
import cn.ruleengine.web.store.mapper.RuleEngineDataReferenceMapper;
import cn.ruleengine.web.vo.condition.*;
import cn.ruleengine.web.vo.reference.ReferenceData;
import cn.ruleengine.web.vo.reference.ReferenceDataMap;
import cn.ruleengine.web.vo.rule.general.DefaultAction;
import cn.ruleengine.web.vo.rule.general.GeneralRuleBody;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 〈DataReferenceServiceImpl〉
 *
 * @author 丁乾文
 * @date 2021/7/27 2:21 下午
 * @since 1.0.0
 */
@Service
public class DataReferenceServiceImpl implements DataReferenceService {

    @Resource
    private RuleEngineDataReferenceManager ruleEngineDataReferenceManager;
    @Resource
    private RuleEngineDataReferenceMapper ruleEngineDataReferenceMapper;
    @Resource
    private RuleEngineGeneralRuleManager ruleEngineGeneralRuleManager;
    @Resource
    private RuleEngineRuleManager ruleEngineRuleManager;
    @Resource
    private RuleEngineConditionManager ruleEngineConditionManager;
    @Resource
    private RuleEngineFunctionValueManager ruleEngineFunctionValueManager;
    @Resource
    private RuleEngineInputParameterManager ruleEngineInputParameterManager;
    @Resource
    private RuleEngineVariableManager ruleEngineVariableManager;
    @Resource
    private RuleEngineGeneralRulePublishManager ruleEngineGeneralRulePublishManager;

    /**
     * 是否有引用这个数据
     *
     * @param type      元素、变量、条件、规则等
     * @param refDataId 元素id...
     */
    @Override
    public void validDataReference(Integer type, Integer refDataId) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(refDataId);
        // 查询基本规则被引用时，不需要判断自己了，只能被规则集合引用
        if (!VariableType.GENERAL_RULE.getType().equals(type)) {
            if (this.ruleEngineGeneralRuleManager.lambdaQuery()
                    .eq(RuleEngineGeneralRule::getDefaultActionType, type)
                    .eq(RuleEngineGeneralRule::getDefaultActionValue, refDataId)
                    .exists()) {
                throw new ValidException("有普通规则默认结果在引用，无法删除");
            }
        }
        if (this.ruleEngineRuleManager.lambdaQuery()
                .eq(RuleEngineRule::getActionType, type)
                .eq(RuleEngineRule::getActionValue, refDataId)
                .exists()) {
            throw new ValidException("有规则结果在引用，无法删除");
        }
        if (this.ruleEngineFunctionValueManager.lambdaQuery()
                .eq(RuleEngineFunctionValue::getType, type)
                .eq(RuleEngineFunctionValue::getValue, refDataId).exists()) {
            throw new ValidException("有函数值在引用，无法删除");
        }
        if (ruleEngineConditionManager.lambdaQuery()
                .and(a -> a.or(o -> o.eq(RuleEngineCondition::getLeftType, type)
                        .eq(RuleEngineCondition::getLeftValue, refDataId))

                        .or(o -> o.eq(RuleEngineCondition::getRightType, type)
                                .eq(RuleEngineCondition::getRightValue, refDataId)
                        )).exists()) {
            throw new ValidException("有条件在引用，无法删除");
        }
        RuleEngineDataReference ruleEngineDataReference = this.ruleEngineDataReferenceMapper.dataReference(type, refDataId);
        if (ruleEngineDataReference != null) {
            throw new ValidException("有发布/待发布数据在引用，无法删除");
        }
    }


    /**
     * 保存规则引用的基础数据
     *
     * @param generalRuleBody g
     * @param version         版本
     */
    @Override
    public void saveDataReference(GeneralRuleBody generalRuleBody, String version) {
        RuleEngineDataReference dataReference = this.ruleEngineDataReferenceManager.lambdaQuery()
                .eq(RuleEngineDataReference::getDataType, DataType.GENERAL_RULE.getType())
                .eq(RuleEngineDataReference::getDataId, generalRuleBody.getId())
                .eq(RuleEngineDataReference::getVersion, version)
                .one();
        ReferenceData referenceData = this.countReferenceData(generalRuleBody);
        String json = referenceData.toJson();
        if (dataReference != null) {
            dataReference.setReferenceData(json);
            this.ruleEngineDataReferenceManager.updateById(dataReference);
        } else {
            RuleEngineDataReference ruleEngineDataReference = new RuleEngineDataReference();
            ruleEngineDataReference.setDataType(DataType.GENERAL_RULE.getType());
            ruleEngineDataReference.setDataId(generalRuleBody.getId());
            ruleEngineDataReference.setReferenceData(json);
            ruleEngineDataReference.setVersion(version);
            this.ruleEngineDataReferenceManager.save(ruleEngineDataReference);
        }
    }

    /**
     * 引用的参数
     *
     * @param referenceData r
     * @return map
     */
    @Override
    public Map<String, Parameter> referenceInputParamList(ReferenceData referenceData) {
        Set<Integer> inputParameterIds = referenceData.getInputParameterIds();
        if (CollUtil.isEmpty(inputParameterIds)) {
            return Collections.emptyMap();
        }
        List<RuleEngineInputParameter> list = this.ruleEngineInputParameterManager.lambdaQuery()
                .in(RuleEngineInputParameter::getId, inputParameterIds).list();
        return list.stream().map(m -> {
            Parameter parameter = new Parameter();
            parameter.setName(m.getName());
            parameter.setCode(m.getCode());
            parameter.setValueType(m.getValueType());
            return parameter;
        }).collect(Collectors.toMap(Parameter::getCode, Function.identity(),
                // 如果code相同
                (o, n) -> o));
    }

    /**
     * 条件组引用的变量以及元素
     *
     * @param referenceData  referenceData
     * @param conditionGroup 条件组
     */
    private void countReferenceData(ReferenceData referenceData, List<ConditionGroupConfig> conditionGroup) {
        for (ConditionGroupConfig conditionGroupConfig : conditionGroup) {
            List<ConditionGroupCondition> conditionGroupCondition = conditionGroupConfig.getConditionGroupCondition();
            // 已知bug修复
            if (CollUtil.isEmpty(conditionGroupCondition)) {
                continue;
            }
            for (ConditionGroupCondition groupCondition : conditionGroupCondition) {
                ConditionBody condition = groupCondition.getCondition();
                ConfigBean config = condition.getConfig();
                this.resolve(referenceData, config.getLeftValue());
                this.resolve(referenceData, config.getRightValue());
            }
        }
    }

    /**
     * 统计普通规则引用的变量以及元素id
     *
     * @param generalRuleBody 普通规则
     * @return ReferenceData
     */
    private ReferenceData countReferenceData(GeneralRuleBody generalRuleBody) {
        ReferenceData referenceData = new ReferenceData();
        this.resolve(referenceData, generalRuleBody.getAction());
        DefaultAction defaultAction = generalRuleBody.getDefaultAction();
        this.resolve(referenceData, defaultAction);
        this.countReferenceData(referenceData, generalRuleBody.getConditionGroup());
        return referenceData;
    }

    /**
     * 解析configValue
     *
     * @param referenceData r
     * @param configValue   c
     */
    public void resolve(ReferenceData referenceData, ConfigValue configValue) {
        if (configValue == null) {
            return;
        }
        Integer type = configValue.getType();
        if (type == null) {
            return;
        }
        String value = configValue.getValue();
        if (value == null) {
            return;
        }
        if (VariableType.INPUT_PARAMETER.getType().equals(type)) {
            referenceData.addInputParameterId(Integer.valueOf(value));
        } else if (VariableType.VARIABLE.getType().equals(type)) {
            referenceData.addVariableId(Integer.valueOf(value));
        } else if (VariableType.GENERAL_RULE.getType().equals(type)) {
            referenceData.addGeneralRuleId(Integer.valueOf(value));
        }
    }

    /**
     * 更新到开发状态
     *
     * @param dataType d
     * @param dataId   id
     */
    @Override
    public void updateToDevStatus(Integer dataType, Integer dataId) {
        DataType dt = DataType.getByType(dataType);
        switch (dt) {
            case GENERAL_RULE:
                this.ruleEngineGeneralRuleManager.lambdaUpdate().eq(RuleEngineGeneralRule::getId, dataId)
                        .set(RuleEngineGeneralRule::getStatus, DataStatus.DEV.getStatus())
                        .update();
                break;
            case RULE_SET:
                break;
            case DECISION_TABLE:
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + dt);
        }
    }

    private ReferenceData getReferenceData(Integer dataType, Integer dataId, String version) {
        RuleEngineDataReference dataReference = this.ruleEngineDataReferenceManager.lambdaQuery()
                .eq(RuleEngineDataReference::getDataType, dataType)
                .eq(RuleEngineDataReference::getDataId, dataId)
                .eq(RuleEngineDataReference::getVersion, version)
                .one();
        if (dataReference == null) {
            return null;
        }
        return JSON.parseObject(dataReference.getReferenceData(), ReferenceData.class);
    }

    /**
     * 缓存数据到ReferenceDataMap
     *
     * @param dataType 数据类型
     * @param dataId   id
     * @param version  版本号
     * @return r
     */
    @Override
    public ReferenceDataMap getReferenceDataMap(Integer dataType, Integer dataId, String version) {
        ReferenceData referenceData = this.getReferenceData(dataType, dataId, version);
        if (referenceData == null) {
            return new ReferenceDataMap();
        }
        ReferenceDataMap referenceDataMap = new ReferenceDataMap();
        Set<Integer> inputParameterIds = referenceData.getInputParameterIds();
        if (CollUtil.isNotEmpty(inputParameterIds)) {
            List<RuleEngineInputParameter> list = this.ruleEngineInputParameterManager.lambdaQuery()
                    .in(RuleEngineInputParameter::getId, inputParameterIds).list();
            referenceDataMap.addRuleEngineInputParameter(list);
        }
        Set<Integer> variableIds = referenceData.getVariableIds();
        if (CollUtil.isNotEmpty(variableIds)) {
            List<RuleEngineVariable> list = this.ruleEngineVariableManager.lambdaQuery()
                    .in(RuleEngineVariable::getId, inputParameterIds).list();
            referenceDataMap.addRuleEngineVariable(list);
        }
        Set<Integer> generalRuleIds = referenceData.getGeneralRuleIds();
        if (CollUtil.isNotEmpty(generalRuleIds)) {
            List<RuleEngineGeneralRule> list = this.ruleEngineGeneralRuleManager.lambdaQuery()
                    .in(RuleEngineGeneralRule::getId, inputParameterIds).list();
            referenceDataMap.addRuleEngineGeneralRule(list);
        }
        return referenceDataMap;
    }

    /**
     * 获取规则集请求参数
     *
     * @param id      规则集id
     * @param version 版本
     * @return param
     */
    @Override
    public Collection<Parameter> getRuleSetParameters(Integer id, String version) {
        ReferenceData referenceData = this.getReferenceData(DataType.RULE_SET.getType(), id, version);
        if (referenceData == null) {
            return Collections.emptyList();
        }
        Set<Integer> dataInputParameterIds = referenceData.getInputParameterIds();
        Set<Integer> variableIds = referenceData.getVariableIds();
        Set<Integer> generalRuleIds = referenceData.getGeneralRuleIds();
        // 寻找基本规则中的引用
        if (CollUtil.isNotEmpty(generalRuleIds)) {
            List<RuleEngineGeneralRulePublish> generalRulePublishs = this.ruleEngineGeneralRulePublishManager.lambdaQuery()
                    .eq(RuleEngineGeneralRulePublish::getStatus, DataStatus.PRD.getStatus())
                    .in(RuleEngineGeneralRulePublish::getGeneralRuleId, generalRuleIds)
                    .list();
            for (RuleEngineGeneralRulePublish generalRulePublish : generalRulePublishs) {
                ReferenceData rd = this.getReferenceData(DataType.GENERAL_RULE.getType(), generalRulePublish.getGeneralRuleId(), generalRulePublish.getVersion());
                if (rd != null) {
                    dataInputParameterIds.addAll(rd.getInputParameterIds());
                    Set<Integer> rdVariableIds = rd.getVariableIds();
                    variableIds.addAll(rdVariableIds);
                }
            }
        }
        return this.processParameters(variableIds, dataInputParameterIds);
    }

    /**
     * 处理参数以及处理变量中的参数
     *
     * @param variableIds           变量中的参数
     * @param dataInputParameterIds 参数id
     * @return param
     */
    private Collection<Parameter> processParameters(Set<Integer> variableIds, Set<Integer> dataInputParameterIds) {
        Set<RuleEngineInputParameter> parameters = new HashSet<>();
        if (CollUtil.isNotEmpty(variableIds)) {
            List<RuleEngineVariable> engineVariables = this.ruleEngineVariableManager.lambdaQuery()
                    .in(RuleEngineVariable::getId, variableIds)
                    .list();
            Set<String> inputParameterCodes = new HashSet<>();
            for (RuleEngineVariable engineVariable : engineVariables) {
                if (engineVariable.getType().equals(VariableType.FORMULA.getType())) {
                    Formula.ExpressionProcessor formulaProcessor = new Formula.ExpressionProcessor(engineVariable.getValue());
                    inputParameterCodes.addAll(formulaProcessor.getInputParameterCodes());
                }
            }
            if (CollUtil.isNotEmpty(inputParameterCodes)) {
                List<RuleEngineInputParameter> inputParameterList = this.ruleEngineInputParameterManager.lambdaQuery()
                        // bug
                        .eq(RuleEngineInputParameter::getWorkspaceId, Context.getCurrentWorkspace().getId())
                        .in(RuleEngineInputParameter::getCode, inputParameterCodes)
                        .list();
                parameters.addAll(inputParameterList);
            }
        }
        if (CollUtil.isNotEmpty(dataInputParameterIds)) {
            List<RuleEngineInputParameter> inputParameterList = this.ruleEngineInputParameterManager.lambdaQuery()
                    // bug
                    .eq(RuleEngineInputParameter::getWorkspaceId, Context.getCurrentWorkspace().getId())
                    .in(RuleEngineInputParameter::getId, dataInputParameterIds)
                    .list();
            parameters.addAll(inputParameterList);
        }
        if (CollUtil.isNotEmpty(parameters)) {
            return parameters.stream().map(m -> {
                Parameter parameter = new Parameter();
                parameter.setName(m.getName());
                parameter.setCode(m.getCode());
                parameter.setValueType(m.getValueType());
                return parameter;
            }).collect(Collectors.toSet());
        }
        return Collections.emptyList();
    }

    /**
     * 获取规则请求参数
     *
     * @param id      规则集id
     * @param version 版本
     * @return param
     */
    @Override
    public Collection<Parameter> getGeneralRuleParameters(Integer id, String version) {
        ReferenceData referenceData = this.getReferenceData(DataType.GENERAL_RULE.getType(), id, version);
        if (referenceData == null) {
            return Collections.emptyList();
        }
        Set<Integer> inputParameterIds = referenceData.getInputParameterIds();
        Set<Integer> variableIds = referenceData.getVariableIds();
        return this.processParameters(variableIds, inputParameterIds);
    }

}
