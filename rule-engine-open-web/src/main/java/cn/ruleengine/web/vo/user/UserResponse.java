package cn.ruleengine.web.vo.user;

import lombok.Data;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author liqian
 * @date 2020/9/24
 */
@Data
public class UserResponse {

    private Integer id;

    private String username;

    private String email;

    private Long phone;

    private String avatar;

    private String sex;

    private Boolean isAdmin;

    private String description;

}
