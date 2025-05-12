package cn.ruleengine.web.service.impl;

import cn.hutool.core.lang.Validator;
import cn.ruleengine.core.value.*;
import cn.ruleengine.web.exception.ApiException;
import cn.ruleengine.web.service.ValueResolve;
import cn.ruleengine.web.store.entity.RuleEngineGeneralRule;
import cn.ruleengine.web.store.entity.RuleEngineInputParameter;
import cn.ruleengine.web.store.entity.RuleEngineVariable;
import cn.ruleengine.web.store.manager.RuleEngineGeneralRuleManager;
import cn.ruleengine.web.store.manager.RuleEngineInputParameterManager;
import cn.ruleengine.web.store.manager.RuleEngineVariableManager;
import cn.ruleengine.web.vo.condition.ConfigValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/7/17
 * @since 1.0.0
 */
@Slf4j
@Component
public class ValueResolveImpl implements ValueResolve {

    @Resource
    private RuleEngineInputParameterManager ruleEngineInputParameterManager;
    @Resource
    private RuleEngineVariableManager ruleEngineVariableManager;
    @Resource
    private RuleEngineGeneralRuleManager ruleEngineGeneralRuleManager;

    /**
     * 解析值，变为Value
     *
     * @param type                        0规则参数，1变量，2固定值
     * @param valueTypeStr                STRING,COLLECTION,BOOLEAN,NUMBER
     * @param value                       type=0则为规则参数id，type=2则为具体的值
     * @param ruleEngineInputParameterMap 缓存数据
     * @return value
     */
    @Override
    public Value getValue(Integer type, String valueTypeStr, String value, Map<Integer, RuleEngineInputParameter> ruleEngineInputParameterMap) {
        VariableType variableTypeEnum = VariableType.getByType(type);
        ValueType valueType = ValueType.getByValue(valueTypeStr);
        switch (variableTypeEnum) {
            case INPUT_PARAMETER:
                RuleEngineInputParameter ruleEngineInputParameter = ruleEngineInputParameterMap.get(Integer.valueOf(value));
                if (ruleEngineInputParameter == null) {
                    throw new ApiException("缺失参数：" + value);
                }
                return new InputParameter(ruleEngineInputParameter.getId(), ruleEngineInputParameter.getCode(), valueType);
            case VARIABLE:
                return new Variable(Integer.valueOf(value), valueType);
            case CONSTANT:
                return new Constant(value, valueType);
            case FORMULA:
                return new Formula(value, valueType);
            case GENERAL_RULE:
                RuleEngineGeneralRule ruleEngineGeneralRule = this.ruleEngineGeneralRuleManager.getById(value);
                if (ruleEngineGeneralRule == null) {
                    throw new ApiException("缺失普通规则：" + value);
                }
                return new Executor(ruleEngineGeneralRule.getWorkspaceCode(), ruleEngineGeneralRule.getId(), ruleEngineGeneralRule.getCode(), valueType);
            default:
                throw new IllegalStateException("Unexpected value: " + variableTypeEnum);
        }
    }

    /**
     * 解析值，变为Value
     *
     * @param type         0规则参数，1变量，2固定值
     * @param valueTypeStr STRING,COLLECTION,BOOLEAN,NUMBER
     * @param value        type=0则为规则参数id，type=2则为具体的值
     * @return value
     */
    @Override
    public Value getValue(Integer type, String valueTypeStr, String value) {
        VariableType variableTypeEnum = VariableType.getByType(type);
        ValueType valueType = ValueType.getByValue(valueTypeStr);
        switch (variableTypeEnum) {
            case INPUT_PARAMETER:
                RuleEngineInputParameter ruleEngineInputParameter = this.ruleEngineInputParameterManager.getById(value);
                if (ruleEngineInputParameter == null) {
                    throw new ApiException("缺失参数：" + value);
                }
                return new InputParameter(ruleEngineInputParameter.getId(), ruleEngineInputParameter.getCode(), valueType);
            case VARIABLE:
                return new Variable(Integer.valueOf(value), valueType);
            case CONSTANT:
                return new Constant(value, valueType);
            case FORMULA:
                return new Formula(value, valueType);
            case GENERAL_RULE:
                RuleEngineGeneralRule ruleEngineGeneralRule = this.ruleEngineGeneralRuleManager.getById(value);
                if (ruleEngineGeneralRule == null) {
                    throw new ApiException("缺失普通规则：" + value);
                }
                return new Executor(ruleEngineGeneralRule.getWorkspaceCode(), ruleEngineGeneralRule.getId(), ruleEngineGeneralRule.getCode(), valueType);
            default:
                throw new IllegalStateException("Unexpected value: " + variableTypeEnum);
        }
    }


    /**
     * 解析值/变量/规则参数/固定值
     *
     * @param cValue Value
     * @return ConfigBean.Value
     */
    @Override
    public ConfigValue getConfigValue(Value cValue) {
        ConfigValue value = new ConfigValue();
        value.setValueType(cValue.getValueType().name());
        if (cValue instanceof Constant) {
            value.setType(VariableType.CONSTANT.getType());
            Constant constant = (Constant) cValue;
            value.setValue(String.valueOf(constant.getValue()));
            value.setValueName(String.valueOf(constant.getValue()));
        } else if (cValue instanceof InputParameter) {
            value.setType(VariableType.INPUT_PARAMETER.getType());
            InputParameter inputParameter = (InputParameter) cValue;
            RuleEngineInputParameter ruleEngineInputParameter = this.ruleEngineInputParameterManager.getById(inputParameter.getInputParameterId());
            if (ruleEngineInputParameter == null) {
                throw new ApiException("缺失参数：" + inputParameter.getInputParameterId());
            }
            value.setValue(String.valueOf(inputParameter.getInputParameterId()));
            value.setValueName(ruleEngineInputParameter.getName());
        } else if (cValue instanceof Variable) {
            value.setType(VariableType.VARIABLE.getType());
            Variable variable = (Variable) cValue;
            value.setValue(String.valueOf(variable.getVariableId()));
            RuleEngineVariable engineVariable = this.ruleEngineVariableManager.getById(variable.getVariableId());
            if (engineVariable == null) {
                throw new ApiException("缺失变量：" + variable.getVariableId());
            }
            value.setVariableType(engineVariable.getType());
            value.setValueName(engineVariable.getName());
            if (engineVariable.getType().equals(VariableType.CONSTANT.getType())) {
                value.setVariableValue(engineVariable.getValue());
            } else if (engineVariable.getType().equals(VariableType.FORMULA.getType())) {
                // 表达式配置
                value.setVariableValue(engineVariable.getValue());
            }
        } else if (cValue instanceof Executor) {
            value.setType(VariableType.GENERAL_RULE.getType());
            Executor executor = (Executor) cValue;
            RuleEngineGeneralRule ruleEngineGeneralRule = this.ruleEngineGeneralRuleManager.getById(executor.getId());
            if (ruleEngineGeneralRule == null) {
                throw new ApiException("缺失普通规则：" + executor.getId());
            }
            value.setValueName(ruleEngineGeneralRule.getName());
        }
        return value;
    }

    /**
     * 解析值/变量/规则参数/固定值
     *
     * @param value     结果值/可能为变量/规则参数
     * @param type      变量/规则参数/固定值
     * @param valueType STRING/NUMBER...
     * @return Action
     */
    @Override
    public ConfigValue getConfigValue(String value, Integer type, String valueType) {
        ConfigValue configValue = new ConfigValue();
        if (Validator.isEmpty(type)) {
            return configValue;
        }
        configValue.setValueType(valueType);
        configValue.setType(type);
        if (Validator.isEmpty(value)) {
            return configValue;
        }
        if (type.equals(VariableType.INPUT_PARAMETER.getType())) {
            RuleEngineInputParameter engineInputParameter = this.ruleEngineInputParameterManager.getById(value);
            if (engineInputParameter == null) {
                throw new ApiException("缺失参数：" + value);
            }
            configValue.setValueName(engineInputParameter.getName());
        } else if (type.equals(VariableType.VARIABLE.getType())) {
            RuleEngineVariable engineVariable = this.ruleEngineVariableManager.getById(value);
            if (engineVariable == null) {
                throw new ApiException("缺失变量：" + value);
            }
            configValue.setValueName(engineVariable.getName());
            configValue.setVariableType(engineVariable.getType());
            if (engineVariable.getType().equals(VariableType.CONSTANT.getType())) {
                configValue.setVariableValue(engineVariable.getValue());
            } else if (engineVariable.getType().equals(VariableType.FORMULA.getType())) {
                // 表达式配置
                configValue.setVariableValue(engineVariable.getValue());
            }
        } else if (type.equals(VariableType.GENERAL_RULE.getType())) {
            RuleEngineGeneralRule ruleEngineGeneralRule = this.ruleEngineGeneralRuleManager.getById(value);
            if (ruleEngineGeneralRule == null) {
                throw new ApiException("缺失普通规则：" + value);
            }
            configValue.setValueName(ruleEngineGeneralRule.getName());
        }
        configValue.setValue(value);
        return configValue;
    }

    /**
     * 如果是变量，查询到变量name，如果是规则参数查询到规则参数name
     *
     * @param type                        类型 变量/规则参数/固定值
     * @param value                       值
     * @param valueType                   值类型 STRING/NUMBER...
     * @param variableMap                 变量缓存
     * @param ruleEngineInputParameterMap 规则参数缓存
     * @return ConfigValue
     */
    @Override
    public ConfigValue getConfigValue(String value, Integer type, String valueType, Map<Integer, RuleEngineVariable> variableMap, Map<Integer, RuleEngineInputParameter> ruleEngineInputParameterMap) {
        String valueName = value;
        String variableValue = null;
        Integer variableType = null;
        if (type.equals(VariableType.INPUT_PARAMETER.getType())) {
            valueName = ruleEngineInputParameterMap.get(Integer.valueOf(value)).getName();
        } else if (type.equals(VariableType.VARIABLE.getType())) {
            RuleEngineVariable engineVariable = variableMap.get(Integer.valueOf(value));
            if (engineVariable == null) {
                throw new ApiException("缺失变量：" + value);
            }
            variableType = engineVariable.getType();
            valueName = engineVariable.getName();
            if (engineVariable.getType().equals(VariableType.CONSTANT.getType())) {
                variableValue = engineVariable.getValue();
            } else if (engineVariable.getType().equals(VariableType.FORMULA.getType())) {
                // 表达式配置
                variableValue = engineVariable.getValue();
            }
        } else if (type.equals(VariableType.GENERAL_RULE.getType())) {
            RuleEngineGeneralRule ruleEngineGeneralRule = this.ruleEngineGeneralRuleManager.getById(value);
            if (ruleEngineGeneralRule == null) {
                throw new ApiException("缺失普通规则：" + value);
            }
            valueName = ruleEngineGeneralRule.getName();
        }
        ConfigValue configBeanValue = new ConfigValue();
        configBeanValue.setType(type);
        configBeanValue.setValue(value);
        configBeanValue.setValueName(valueName);
        configBeanValue.setVariableValue(variableValue);
        configBeanValue.setValueType(valueType);
        configBeanValue.setVariableType(variableType);
        return configBeanValue;
    }

}
