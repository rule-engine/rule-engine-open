package cn.ruleengine.web.vo.workspace.member;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 〈BindMemberRequest〉
 *
 * @author 丁乾文
 * @date 2021/6/23 4:30 下午
 * @since 1.0.0
 */
@Data
public class BindMemberRequest {

    /**
     * 绑定的用户列表
     */
    @NotNull
    private List<Integer> userList;

    @NotNull
    private Integer workspaceId;

}
