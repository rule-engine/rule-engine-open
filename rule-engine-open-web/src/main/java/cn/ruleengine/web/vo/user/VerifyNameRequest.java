package cn.ruleengine.web.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @date 2019/8/23
 * @since 1.0.0
 */
@Data
@ApiModel("验证用户名是否重复请求参数")
public class VerifyNameRequest {

    @Size(min = 2, max = 10, message = "用户名需要2到10位")
    @NotBlank(message = "用户名不能为空")
    @ApiModelProperty("用户名")
    private String username;
}
