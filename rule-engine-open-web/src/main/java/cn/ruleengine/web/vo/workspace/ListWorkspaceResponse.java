package cn.ruleengine.web.vo.workspace;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/12/19
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ListWorkspaceResponse extends Workspace {

    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 工作空间管理员列表
     */
    private List<AdminUser> workspaceAdminList;

    @Data
    public static class AdminUser {

        private Integer id;

        private String username;

        private String avatar;

    }

}
