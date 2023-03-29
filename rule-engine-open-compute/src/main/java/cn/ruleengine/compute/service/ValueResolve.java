package cn.ruleengine.compute.service;

import cn.ruleengine.compute.store.entity.RuleEngineInputParameter;
import cn.ruleengine.core.value.Value;

import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/7/17
 * @since 1.0.0
 */
public interface ValueResolve {

    /**
     * 解析值，变为Value
     *
     * @param type                    0规则参数，1变量，2固定值
     * @param valueType               STRING,COLLECTION,BOOLEAN,NUMBER
     * @param value                   type=0则为规则参数id，type=2则为具体的值
     * @param engineInputParameterMap engineInputParameterMap
     * @return value
     */
    Value getValue(Integer type, String valueType, String value, Map<Integer, RuleEngineInputParameter> engineInputParameterMap);

    /**
     * 解析值，变为Value
     *
     * @param type      0规则参数，1变量，2固定值
     * @param valueType STRING,COLLECTION,BOOLEAN,NUMBER
     * @param value     type=0则为规则参数id，type=2则为具体的值
     * @return value
     */
    Value getValue(Integer type, String valueType, String value);

}
