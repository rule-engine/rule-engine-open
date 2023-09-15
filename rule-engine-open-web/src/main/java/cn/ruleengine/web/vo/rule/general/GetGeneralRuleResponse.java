package cn.ruleengine.web.vo.rule.general;

import cn.ruleengine.web.vo.condition.ConditionGroupConfig;
import cn.ruleengine.web.vo.condition.ConfigValue;
import lombok.Data;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/24
 * @since 1.0.0
 */
@Data
public class GetGeneralRuleResponse {

    private Integer id;

    private String name;

    private String code;

    private String currentVersion;

    private String publishVersion;

    private String description;

    private Integer workspaceId;

    private String workspaceCode;

    private Integer ruleId;

    private List<ConditionGroupConfig> conditionGroup;

    private ConfigValue action;

    private DefaultAction defaultAction;

}
