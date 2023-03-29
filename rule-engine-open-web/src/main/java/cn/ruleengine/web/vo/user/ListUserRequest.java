package cn.ruleengine.web.vo.user;

import lombok.Data;

/**
 * 〈ListUserRequest〉
 *
 * @author 丁乾文
 * @date 2021/6/23 3:16 下午
 * @since 1.0.0
 */
@Data
public class ListUserRequest {

    private String username;

    private String email;

    private String sex;

}
