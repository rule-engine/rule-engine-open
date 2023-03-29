package cn.ruleengine.web.vo.workspace.member;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 〈OptionalPersonnelRequest〉
 *
 * @author 丁乾文
 * @date 2021/6/23 5:08 下午
 * @since 1.0.0
 */
@Data
public class OptionalPersonnelRequest {

    private String username;

    /**
     * 排除掉此工作空间的用户
     */
    @NotNull
    private Integer workspaceId;

}
