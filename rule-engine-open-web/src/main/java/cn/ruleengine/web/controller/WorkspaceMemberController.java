package cn.ruleengine.web.controller;

import cn.ruleengine.common.vo.BaseResult;
import cn.ruleengine.common.vo.PageRequest;
import cn.ruleengine.common.vo.PageResult;
import cn.ruleengine.common.vo.PlainResult;
import cn.ruleengine.web.annotation.Auth;
import cn.ruleengine.web.service.WorkspaceMemberService;
import cn.ruleengine.web.vo.workspace.member.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 〈WorkspaceMemberController〉
 *
 * @author 丁乾文
 * @date 2021/6/23 10:01 上午
 * @since 1.0.0
 */
@Api(tags = "工作空间成员控制器")
@RestController
@RequestMapping("workspaceMember")
public class WorkspaceMemberController {

    @Resource
    private WorkspaceMemberService workspaceMemberService;

    /**
     * 工作空间下的成员
     *
     * @param pageRequest p
     * @return r
     */
    @PostMapping("list")
    @ApiOperation("工作空间下的成员")
    public PageResult<WorkspaceMember> list(@RequestBody @Valid PageRequest<ListWorkspaceMemberRequest> pageRequest) {
        return this.workspaceMemberService.list(pageRequest);
    }

    /**
     * 绑定成员
     *
     * @param bindMemberRequest b
     * @return r
     */
    @PostMapping("bindMember")
    @ApiOperation("绑定成员")
    public BaseResult bindMember(@RequestBody @Valid BindMemberRequest bindMemberRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(workspaceMemberService.bindMember(bindMemberRequest));
        return plainResult;
    }

    /**
     * 可选工作空间列表人员
     *
     * @param pageRequest p
     * @return r
     */
    @PostMapping("optionalPersonnel")
    @ApiOperation("可选工作空间列表人员")
    public PageResult<WorkspaceMember> optionalPersonnel(@RequestBody @Valid PageRequest<OptionalPersonnelRequest> pageRequest) {
        return this.workspaceMemberService.optionalPersonnel(pageRequest);
    }

    /**
     * 删除成员
     *
     * @param deleteMemberRequest d
     * @return r
     */
    @Auth(accessibleRole = Auth.Role.WORKSPACE_ADMINISTRATOR)
    @PostMapping("deleteMember")
    @ApiOperation("删除成员")
    public BaseResult deleteMember(@RequestBody @Valid DeleteMemberRequest deleteMemberRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(workspaceMemberService.deleteMember(deleteMemberRequest));
        return plainResult;
    }


    /**
     * 转移权限
     *
     * @param permissionTransferRequest p
     * @return r
     */
    @Auth
    @PostMapping("permissionTransfer")
    @ApiOperation("转移权限")
    public BaseResult permissionTransfer(@RequestBody @Valid PermissionTransferRequest permissionTransferRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(workspaceMemberService.permissionTransfer(permissionTransferRequest));
        return plainResult;
    }

}
