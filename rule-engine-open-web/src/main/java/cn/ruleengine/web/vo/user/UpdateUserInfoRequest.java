package cn.ruleengine.web.vo.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/4/5
 * @since 1.0.0
 */
@Data
public class UpdateUserInfoRequest {

    @NotNull
    @ApiModelProperty("用户id")
    private Integer id;

    @ApiModelProperty("用户性别")
    private String sex;


    @ApiModelProperty("用户头像")
    private String avatar;

    @ApiModelProperty("用户手机号")
    private Long phone;

    @Email
    @ApiModelProperty("用户邮箱")
    private String email;

    @ApiModelProperty("个人描述")
    private String description;

}
