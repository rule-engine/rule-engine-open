package cn.ruleengine.web.vo.generalrule;

import cn.ruleengine.web.vo.condition.ConfigValue;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 〈SaveActionRequest〉
 *
 * @author 丁乾文
 * @date 2021/7/12 5:34 下午
 * @since 1.0.0
 */
@Data
public class SaveActionRequest {

    @NotNull
    private Integer ruleId;

    /**
     * 结果配置信息
     */
    @NotNull
    @Valid
    private ConfigValue configValue;


}
