package cn.ruleengine.web.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @date 2019/8/23
 * @since 1.0.0
 */
@Data
@ApiModel("获取验证码请求参数")
public class GetVerifyCodeByEmailRequest {

    @NotNull
    @ApiModelProperty("获取验证码用途类型")
    private Integer type;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式错误")
    @ApiModelProperty("邮箱")
    private String email;
}
