package cn.ruleengine.web.vo.parameter;

import cn.ruleengine.web.vo.common.DataTypeAndId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/***
 * 验证参数code
 * @author niuxiangqian
 * @version 1.0
 * @since 2021/7/15 4:11 下午
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class VerifyInputParameterCodeRequest extends DataTypeAndId {

    @Length(min = 1, max = 25, message = "规则参数Code长度在 1 到 25 个字符")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_&#\\-]*$", message = "规则参数Code只能字母开头，以及字母数字_&#-组成")
    @NotBlank(message = "规则参数编码不能为空")
    private String code;

}
