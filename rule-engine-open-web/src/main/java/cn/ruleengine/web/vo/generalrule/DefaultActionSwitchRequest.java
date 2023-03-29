package cn.ruleengine.web.vo.generalrule;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 〈DefaultActionSwitchRequest〉
 *
 * @author 丁乾文
 * @date 2021/7/15 11:16 下午
 * @since 1.0.0
 */
@Data
public class DefaultActionSwitchRequest {

    @NotNull
    private Integer generalRuleId;

    @NotNull
    private Integer enableDefaultAction;

}
