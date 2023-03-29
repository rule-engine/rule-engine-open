package cn.ruleengine.web.vo.user;

import lombok.Data;


/**
 * @author dqw
 */
@Data
public class SelectUserResponse {

    private Integer id;

    private String username;

    private String email;

    private Long phone;

    private String avatar;

    private String sex;


}
