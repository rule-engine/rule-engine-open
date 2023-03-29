package cn.ruleengine.web.vo.generalrule;

import cn.ruleengine.web.vo.condition.ConditionGroupConfig;
import cn.ruleengine.web.vo.condition.ConfigValue;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @date 2021/1/23
 * @since 1.0.0
 */
@Data
public class GeneralRuleBody {

    @NotNull
    private Integer id;

    @Valid
    private List<ConditionGroupConfig> conditionGroup = new ArrayList<>(1);

    @NotNull
    @Valid
    private ConfigValue action;

    private DefaultAction defaultAction;

}
