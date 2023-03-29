package cn.ruleengine.web.vo.workspace.member;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 〈WorkspaceMemberRequest〉
 *
 * @author 丁乾文
 * @date 2021/6/23 10:43 上午
 * @since 1.0.0
 */
@Data
public class ListWorkspaceMemberRequest {

    @NotNull
    private Integer workspaceId;

    /**
     * 查询管理，还是普通成员
     */
    @NotNull
    private Integer type;

    /**
     * 用户名称  模糊查询
     */
    private String userName;

}
