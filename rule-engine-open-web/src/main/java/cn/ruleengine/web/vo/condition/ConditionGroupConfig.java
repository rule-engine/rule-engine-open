package cn.ruleengine.web.vo.condition;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/28
 * @since 1.0.0
 */
@Data
public class ConditionGroupConfig {

    private Integer id;

    @NotBlank(message = "条件组名称不能为空")
    private String name;

    @NotNull
    private Integer orderNo;

    /**
     * 条件组与条件关系
     */
    @Valid
    private List<ConditionGroupCondition> conditionGroupCondition = new ArrayList<>(1);

}
