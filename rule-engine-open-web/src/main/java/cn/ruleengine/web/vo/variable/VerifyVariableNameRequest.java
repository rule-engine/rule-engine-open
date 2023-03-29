package cn.ruleengine.web.vo.variable;

import cn.ruleengine.web.vo.common.DataTypeAndId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/***
 * 验证变量名字
 * @author niuxiangqian
 * @version 1.0
 * @since 2021/7/15 4:11 下午
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class VerifyVariableNameRequest extends DataTypeAndId {

    @NotBlank
    @Length(min = 1, max = 25, message = "变量名称长度在 1 到 25 个字符")
    private String name;


}
