package cn.ruleengine.web.service;

import cn.ruleengine.common.vo.PageRequest;
import cn.ruleengine.common.vo.PageResult;
import cn.ruleengine.web.vo.workspace.member.*;

/**
 * 〈WorkspaceMemberService〉
 *
 * @author 丁乾文
 * @date 2021/6/23 10:45 上午
 * @since 1.0.0
 */
public interface WorkspaceMemberService {


    /**
     * 工作空间下的成员
     *
     * @param pageRequest p
     * @return r
     */
    PageResult<WorkspaceMember> list(PageRequest<ListWorkspaceMemberRequest> pageRequest);

    /**
     * 绑定成员
     *
     * @param bindMemberRequest b
     * @return r
     */
    Boolean bindMember(BindMemberRequest bindMemberRequest);

    /**
     * 可选工作空间列表人员
     *
     * @param pageRequest p
     * @return r
     */
    PageResult<WorkspaceMember> optionalPersonnel(PageRequest<OptionalPersonnelRequest> pageRequest);

    /**
     * 删除成员
     *
     * @param deleteMemberRequest d
     * @return r
     */
    Boolean deleteMember(DeleteMemberRequest deleteMemberRequest);

    /**
     * 转移权限
     *
     * @param permissionTransferRequest p
     * @return r
     */
    Boolean permissionTransfer(PermissionTransferRequest permissionTransferRequest);

}
