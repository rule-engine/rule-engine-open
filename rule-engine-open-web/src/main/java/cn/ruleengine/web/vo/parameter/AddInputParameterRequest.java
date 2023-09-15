package cn.ruleengine.web.vo.parameter;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/7/14
 * @since 1.0.0
 */
@Data
public class AddInputParameterRequest {

    @Length(min = 1, max = 25, message = "规则参数名称长度在 1 到 25 个字符")
    @NotBlank(message = "规则参数名称不能为空")
    private String name;

    @Length(min = 1, max = 25, message = "规则参数Code长度在 1 到 25 个字符")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_&#\\-]*$", message = "规则参数Code只能字母开头，以及字母数字_&#-组成")
    @NotBlank(message = "规则参数编码不能为空")
    private String code;

    @NotBlank(message = "规则参数类型不能为空")
    private String valueType;

    private String description;

}
