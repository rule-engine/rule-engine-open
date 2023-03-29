package cn.ruleengine.compute.service.impl;

import cn.ruleengine.compute.service.ValueResolve;
import cn.ruleengine.compute.store.entity.RuleEngineInputParameter;
import cn.ruleengine.compute.store.manager.RuleEngineInputParameterManager;
import cn.ruleengine.core.value.*;
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
                return new InputParameter(ruleEngineInputParameter.getId(), ruleEngineInputParameter.getCode(), valueType);
            case VARIABLE:
                return new Variable(Integer.valueOf(value), valueType);
            case CONSTANT:
                return new Constant(value, valueType);
            case FORMULA:
                return new Formula(value, valueType);
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
                return new InputParameter(ruleEngineInputParameter.getId(), ruleEngineInputParameter.getCode(), valueType);
            case VARIABLE:
                return new Variable(Integer.valueOf(value), valueType);
            case CONSTANT:
                return new Constant(value, valueType);
            case FORMULA:
                return new Formula(value, valueType);
            default:
                throw new IllegalStateException("Unexpected value: " + variableTypeEnum);
        }
    }


}
