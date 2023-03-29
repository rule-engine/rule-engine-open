package cn.ruleengine.web.vo.datareference;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @date 2021/1/23
 * @since 1.0.0
 */
@Data
public class ReferenceData {

    private Set<Integer> inputParameterIds = new HashSet<>();
    private Set<Integer> variableIds = new HashSet<>();
    /**
     * 引用的表达式
     */
    private Set<Integer> formulaIds = new HashSet<>();
    /**
     * 引用的普通规则
     * 目前规则集可以引用
     */
    private Set<Integer> generalRuleIds = new HashSet<>();


    public void addVariableId(Integer id) {
        this.variableIds.add(id);
    }

    public void addInputParameterId(Integer id) {
        this.inputParameterIds.add(id);
    }

    public void addGeneralRuleId(Integer id) {
        this.generalRuleIds.add(id);
    }

    public void addFormulaId(Integer id) {
        this.formulaIds.add(id);
    }

    /**
     * 转为json
     *
     * @return json
     */
    public String toJson() {
        return JSON.toJSONString(this);
    }


    public void append(ReferenceData rd) {
        this.inputParameterIds.addAll(rd.getInputParameterIds());
        this.variableIds.addAll(rd.getInputParameterIds());
        this.formulaIds.addAll(rd.getFormulaIds());
        this.generalRuleIds.addAll(rd.getGeneralRuleIds());
    }

}
