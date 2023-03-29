package cn.ruleengine.web.vo.workspace;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Administrator
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CurrentWorkspaceInfoResponse extends Workspace {


    private Boolean isAdmin;

}
