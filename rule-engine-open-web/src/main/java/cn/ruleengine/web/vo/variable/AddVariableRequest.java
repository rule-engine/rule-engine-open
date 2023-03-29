package cn.ruleengine.web.vo.variable;

import cn.ruleengine.web.vo.common.DataTypeAndId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/7/14
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AddVariableRequest extends DataTypeAndId {

    @NotBlank
    @Length(min = 1, max = 25, message = "变量名称长度在 1 到 25 个字符")
    private String name;

    @NotNull
    private Integer type;

    private String description;

    @NotBlank
    private String valueType;

    @NotNull
    private String value;

    /**
     * 函数
     */
    @NotNull
    private VariableFunction function;


}
