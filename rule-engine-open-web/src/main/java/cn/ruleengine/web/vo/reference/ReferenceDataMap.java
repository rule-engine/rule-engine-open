package cn.ruleengine.web.vo.reference;


import cn.hutool.core.collection.CollUtil;
import cn.ruleengine.web.store.entity.RuleEngineFormula;
import cn.ruleengine.web.store.entity.RuleEngineGeneralRule;
import cn.ruleengine.web.store.entity.RuleEngineInputParameter;
import cn.ruleengine.web.store.entity.RuleEngineVariable;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @date 2021/1/23
 * @since 1.0.0
 */
public class ReferenceDataMap {

    @Getter
    private final Map<Integer, RuleEngineInputParameter> inputParameterMap = new HashMap<>();

    @Getter
    private final Map<Integer, RuleEngineFormula> formulaMap = new HashMap<>();

    @Getter
    private final Map<Integer, RuleEngineVariable> variableMap = new HashMap<>();

    @Getter
    private final Map<Integer, RuleEngineGeneralRule> generalRuleMap = new HashMap<>();

    /**
     * 添加参数缓存
     *
     * @param list 参数list
     */
    public void addRuleEngineInputParameter(List<RuleEngineInputParameter> list) {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        this.inputParameterMap.putAll(list.stream().collect(Collectors.toMap(RuleEngineInputParameter::getId, Function.identity())));
    }

    /**
     * 根据id查询缓存中的参数
     *
     * @param id 参数id
     * @return r
     */
    public RuleEngineInputParameter getInputParameterById(Integer id) {
        return inputParameterMap.get(id);
    }

    /**
     * 根据id查询缓存中的变量
     *
     * @param id 参数id
     * @return r
     */
    public RuleEngineVariable getVariableById(Integer id) {
        return variableMap.get(id);
    }

    /**
     * 根据id查询缓存中的表达式
     *
     * @param id 表达式id
     * @return r
     */
    public RuleEngineFormula getFormulaById(Integer id) {
        return formulaMap.get(id);
    }

    /**
     * 根据id查询缓存中的普通规则
     *
     * @param id 普通规则id
     * @return r
     */
    public RuleEngineGeneralRule getGeneralRuleById(Integer id) {
        return generalRuleMap.get(id);
    }

    /**
     * 添加表达式缓存
     *
     * @param list 表达式list
     */
    public void addRuleEngineFormula(List<RuleEngineFormula> list) {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        this.formulaMap.putAll(list.stream().collect(Collectors.toMap(RuleEngineFormula::getId, Function.identity())));
    }

    /**
     * 添加变量缓存
     *
     * @param list 变量list
     */
    public void addRuleEngineVariable(List<RuleEngineVariable> list) {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        this.variableMap.putAll(list.stream().collect(Collectors.toMap(RuleEngineVariable::getId, Function.identity())));
    }

    /**
     * 添加普通规则缓存
     *
     * @param list 普通规则list
     */
    public void addRuleEngineGeneralRule(List<RuleEngineGeneralRule> list) {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        this.generalRuleMap.putAll(list.stream().collect(Collectors.toMap(RuleEngineGeneralRule::getId, Function.identity())));
    }

}
