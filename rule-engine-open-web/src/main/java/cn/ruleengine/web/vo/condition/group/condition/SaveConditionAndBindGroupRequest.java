package cn.ruleengine.web.vo.condition.group.condition;

import cn.ruleengine.web.vo.common.DataTypeAndId;
import cn.ruleengine.web.vo.condition.AddConditionRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 〈SaveConditionAndBindGroupRequest〉
 *
 * @author 丁乾文
 * @date 2021/7/12 1:42 下午
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SaveConditionAndBindGroupRequest extends DataTypeAndId {

    /**
     * 与addConditionRequest绑定
     */
    @NotNull
    private Integer conditionGroupId;

    @NotNull
    private Integer orderNo;

    /**
     * 条件信息
     */
    @NotNull
    @Valid
    private AddConditionRequest addConditionRequest;

}
