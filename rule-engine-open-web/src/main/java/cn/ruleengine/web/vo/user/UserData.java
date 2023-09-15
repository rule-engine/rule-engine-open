package cn.ruleengine.web.vo.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @date 2020/11/22
 * @since 1.0.0
 */
@Data
public class UserData implements Serializable {

    private static final long serialVersionUID = -5944149026431724373L;

    private Integer id;

    private String username;

    private String email;

    private Long phone;

    private String avatar;

    private String sex;

    private Integer isAdmin;

    private String description;

    public Boolean getIsAdmin() {
        if (this.isAdmin == null) {
            return false;
        }
        return this.isAdmin == 0;
    }

}
