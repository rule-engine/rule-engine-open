package cn.ruleengine.web.vo.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/***
 * 注册邮箱检查
 * @author niuxiangqian
 * @version 1.0
 * @since 2021/7/14 3:08 下午
 **/
@Data
public class VerifyUserEmailRequest {

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式错误")
    @ApiModelProperty(value = "邮箱",required = true)
    private String email;
}
