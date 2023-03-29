package cn.ruleengine.web.vo.condition;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/25
 * @since 1.0.0
 */
@Data
public class UpdateConditionRequest {
    @NotNull
    private Integer id;

    @Length(min = 1, max = 25, message = "条件名称长度在 1 到 25 个字符")
    @NotBlank(message = "条件名称不能为空")
    private String name;

    private String description;

    @NotNull(message = "条件配置不能为空")
    @Valid
    private ConfigBean config;

}
