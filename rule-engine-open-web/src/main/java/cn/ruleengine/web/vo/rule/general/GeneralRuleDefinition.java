package cn.ruleengine.web.vo.rule.general;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/28
 * @since 1.0.0
 */
@Data
public class GeneralRuleDefinition {

    private Integer id;

    @NotBlank
    @Length(min = 1, max = 25, message = "规则名称长度在 1 到 25 个字符")
    private String name;

    @NotBlank
    @Length(min = 1, max = 25, message = "规则编码长度在 1 到 25 个字符")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_&#\\-]*$", message = "规则Code只能字母开头，以及字母数字_&#-组成")
    private String code;

    private String description;


}
