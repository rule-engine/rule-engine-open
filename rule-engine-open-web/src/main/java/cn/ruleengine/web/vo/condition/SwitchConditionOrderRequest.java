package cn.ruleengine.web.vo.condition;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/***
 * 交换条件顺序
 * @author niuxiangqian
 * @version 1.0
 * @since 2021/7/17 5:53 下午
 **/
@Data
public class SwitchConditionOrderRequest {

    public static final Integer TOP = 0;

    public static final Integer BOTTOM = 1;

    @NotNull
    @ApiModelProperty(value = "原来的id", required = true)
    private Integer fromId;

    @NotNull
    private Integer fromConditionGroupId;

    /**
     * 可以不传，只有当跨条件组拖拽时候  目前条件组没有任何条件时
     */
    @ApiModelProperty(value = "目标id", required = true)
    private Integer toId;

    /**
     * 0 是 toId的上面  1是toId的下面
     */
    private Integer toType = 1;

    @NotNull
    private Integer toConditionGroupId;

}
