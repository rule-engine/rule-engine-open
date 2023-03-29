package cn.ruleengine.web.vo.workspace.member;

import lombok.Data;

/**
 * 〈WorkspaceMember〉
 *
 * @author 丁乾文
 * @date 2021/6/23 10:42 上午
 * @since 1.0.0
 */
@Data
public class WorkspaceMember {

    private Integer userId;

    private String username;

    private String avatar;

    private String email;

}
