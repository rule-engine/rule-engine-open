package cn.ruleengine.web.vo.rule.general;

import cn.ruleengine.core.Parameter;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collection;


/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/24
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ViewGeneralRuleResponse extends GetGeneralRuleResponse {

    /**
     * 规则入参
     */
    private Collection<Parameter> parameters;

}
