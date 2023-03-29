package cn.ruleengine.web.vo.workspace.member;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 〈DeleteMemberRequest〉
 *
 * @author 丁乾文
 * @date 2021/6/23 5:30 下午
 * @since 1.0.0
 */
@Data
public class DeleteMemberRequest {

    @NotNull
    private Integer workspaceId;

    @NotNull
    private Integer userId;

}
