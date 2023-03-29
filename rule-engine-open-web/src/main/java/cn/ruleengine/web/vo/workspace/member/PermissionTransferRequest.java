package cn.ruleengine.web.vo.workspace.member;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 〈PermissionTransferRequest〉
 *
 * @author 丁乾文
 * @date 2021/6/23 7:43 下午
 * @since 1.0.0
 */
@Data
public class PermissionTransferRequest {

    @NotNull
    private Integer workspaceId;

    @NotNull
    private Integer userId;

    @NotNull
    private Integer type;
}
