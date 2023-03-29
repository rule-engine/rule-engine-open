package cn.ruleengine.web.vo.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * 〈AddUserRequest〉
 *
 * @author 丁乾文
 * @date 2021/6/23 3:53 下午
 * @since 1.0.0
 */
@Data
public class AddUserRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @Email
    private String email;

    private String phone;

    private String sex;

}
