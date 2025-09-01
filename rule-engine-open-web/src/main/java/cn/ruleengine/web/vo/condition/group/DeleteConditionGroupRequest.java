package cn.ruleengine.web.vo.condition.group;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 〈DeleteConditionGroupRequest〉
 *
 * @author 丁乾文
 * @date 2021/9/9 4:49 下午
 * @since 1.0.0
 */
@Data
public class DeleteConditionGroupRequest {

    @NotNull
    private Integer id;

}
