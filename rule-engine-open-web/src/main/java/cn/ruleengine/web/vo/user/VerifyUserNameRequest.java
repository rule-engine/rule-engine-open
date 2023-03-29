package cn.ruleengine.web.vo.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/***
 * 验证用户是否可用request
 * @author niuxiangqian
 * @version 1.0
 * @since 2021/7/14 2:54 下午
 **/
@Data
public class VerifyUserNameRequest {
    @Size(min = 2, max = 10, message = "用户名需要2到10位")
    @NotBlank(message = "用户名不能为空")
    @ApiModelProperty(value = "用户名",required = true)
    private String username;
}
