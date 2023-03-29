/*
 * Copyright (c) 2020 dingqianwen (761945125@qq.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ruleengine.web.controller;

import cn.ruleengine.common.vo.*;
import cn.ruleengine.web.annotation.Auth;
import cn.ruleengine.web.service.WorkspaceService;
import cn.ruleengine.web.vo.workspace.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @date 2020/11/21
 * @since 1.0.0
 */
@Api(tags = "工作空间控制器")
@RestController
@RequestMapping("workspace")
public class WorkspaceController {

    @Resource
    private WorkspaceService workspaceService;

    /**
     * 用户有权限的工作空间列表
     *
     * @param pageRequest 模糊查询参数
     * @return list
     */
    @PostMapping("list")
    @ApiOperation("用户有权限的工作空间列表")
    public BaseResult list(@RequestBody PageRequest<ListWorkspaceRequest> pageRequest) {
        return this.workspaceService.list(pageRequest);
    }

    /**
     * 切换工作空间
     *
     * @param idRequest 工作空间id
     * @return true
     */
    @PostMapping("change")
    @ApiOperation("切换工作空间")
    public BaseResult change(@RequestBody @Valid IdRequest idRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(workspaceService.change(idRequest.getId()));
        return plainResult;
    }

    /**
     * 获取当前工作空间
     *
     * @return Workspace
     */
    @PostMapping("currentWorkspace")
    @ApiOperation("当前工作空间")
    public BaseResult currentWorkspace() {
        PlainResult<CurrentWorkspaceInfoResponse> plainResult = new PlainResult<>();
        plainResult.setData(workspaceService.currentWorkspaceInfo());
        return plainResult;
    }

    /**
     * 根据工作空间code查询工作空间AccessKey
     * <p>
     * 记录一个bug 可以查询别人的工作空间accessKey
     *
     * @return accessKey
     */
    @Auth(accessibleRole = Auth.Role.WORKSPACE_ADMINISTRATOR)
    @PostMapping("accessKey")
    @ApiOperation("查询工作空间AccessKey")
    public BaseResult accessKey(@RequestBody @Valid Param<String> param) {
        PlainResult<AccessKey> plainResult = new PlainResult<>();
        plainResult.setData(workspaceService.accessKey(param.getParam()));
        return plainResult;
    }

    /**
     * 更新工作空间AccessKey
     *
     * @param param p
     * @return true
     */
    @Auth(accessibleRole = Auth.Role.WORKSPACE_ADMINISTRATOR)
    @PostMapping("updateAccessKey")
    @ApiOperation("更新工作空间AccessKey")
    public BaseResult updateAccessKey(@RequestBody @Valid UpdateAccessKeyRequest param) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(workspaceService.updateAccessKey(param));
        return plainResult;
    }

    /**
     * 添加工作空间
     *
     * @param addWorkspaceRequest a
     * @return r
     */
    @Auth
    @PostMapping("add")
    @ApiOperation("添加工作空间")
    public BaseResult add(@RequestBody @Valid AddWorkspaceRequest addWorkspaceRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(workspaceService.add(addWorkspaceRequest));
        return plainResult;
    }

    /**
     * 验证工作空间code
     *
     * @param verifyWorkspaceRequest 工作空间code
     * @return r
     */
    @Auth(accessibleRole = Auth.Role.SUPER_ADMINISTRATOR)
    @PostMapping("verifyWorkspaceCode")
    @ApiOperation("验证工作空间code")
    public BaseResult checkWorkspaceCode(@RequestBody @Valid VerifyWorkspaceRequest verifyWorkspaceRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(workspaceService.verifyCode(verifyWorkspaceRequest.getCode()));
        return plainResult;
    }

    /**
     * 编辑工作空间
     *
     * @param editWorkspaceRequest e
     * @return r
     */
    @Auth(accessibleRole = Auth.Role.WORKSPACE_ADMINISTRATOR)
    @PostMapping("edit")
    @ApiOperation("编辑工作空间")
    public BaseResult edit(@RequestBody @Valid EditWorkspaceRequest editWorkspaceRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(workspaceService.edit(editWorkspaceRequest));
        return plainResult;
    }

    /**
     * 删除工作空间
     *
     * @param idRequest id
     * @return r
     */
    @Auth
    @PostMapping("delete")
    @ApiOperation("删除工作空间")
    public BaseResult delete(@RequestBody @Valid IdRequest idRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(workspaceService.delete(idRequest.getId()));
        return plainResult;
    }


    /**
     * 通过工作空间id查询工作空间信息
     *
     * @param idRequest id
     * @return r
     */
    @PostMapping("selectWorkSpaceById")
    @ApiOperation("通过工作空间id查询工作空间信息")
    public PlainResult<SelectWorkspaceResponse> selectWorkSpaceById(@RequestBody @Valid IdRequest idRequest) {
        PlainResult<SelectWorkspaceResponse> plainResult = new PlainResult<>();
        plainResult.setData(workspaceService.selectWorkSpaceById(idRequest));
        return plainResult;
    }

}
