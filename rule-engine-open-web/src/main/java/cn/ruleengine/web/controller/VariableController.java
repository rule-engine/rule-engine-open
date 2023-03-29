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


import cn.ruleengine.common.vo.IdRequest;
import cn.ruleengine.common.vo.PageRequest;
import cn.ruleengine.common.vo.PageResult;
import cn.ruleengine.common.vo.PlainResult;
import cn.ruleengine.web.annotation.DataPermission;
import cn.ruleengine.web.annotation.ReSubmitLock;
import cn.ruleengine.web.enums.DataType;
import cn.ruleengine.web.enums.OperationType;
import cn.ruleengine.web.service.VariableService;
import cn.ruleengine.web.vo.variable.*;
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
 * @author dingqianwen
 * @date 2020/7/14
 * @since 1.0.0
 */
@Api(tags = "变量控制器")
@RestController
@RequestMapping("ruleEngine/variable")
public class VariableController {

    @Resource
    private VariableService variableService;

    /**
     * 添加变量
     *
     * @param addConditionRequest 变量信息
     * @return true
     */
    @ReSubmitLock
    @PostMapping("add")
    @ApiOperation("添加变量")
    public PlainResult<Boolean> add(@RequestBody @Valid AddVariableRequest addConditionRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(variableService.add(addConditionRequest));
        return plainResult;
    }

    /**
     * 根据id查询变量
     *
     * @param idRequest 变量id
     * @return var
     */
    @DataPermission(id = "#idRequest.id", dataType = DataType.VARIABLE, operationType = OperationType.SELECT)
    @PostMapping("get")
    @ApiOperation("根据id查询变量")
    public PlainResult<GetVariableResponse> get(@RequestBody @Valid IdRequest idRequest) {
        PlainResult<GetVariableResponse> plainResult = new PlainResult<>();
        plainResult.setData(variableService.get(idRequest.getId()));
        return plainResult;
    }

    /**
     * 根据id更新变量
     *
     * @param updateVariableRequest param
     * @return true
     */
    @DataPermission(id = "#updateVariableRequest.id", dataType = DataType.VARIABLE, operationType = OperationType.UPDATE)
    @ReSubmitLock
    @PostMapping("update")
    @ApiOperation("根据id更新变量")
    public PlainResult<Boolean> update(@RequestBody @Valid UpdateVariableRequest updateVariableRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(variableService.update(updateVariableRequest));
        return plainResult;
    }

    /**
     * 变量列表
     *
     * @param pageRequest param
     * @return result
     */
    @PostMapping("list")
    @ApiOperation("变量列表")
    public PageResult<ListVariableResponse> list(@RequestBody PageRequest<ListVariableRequest> pageRequest) {
        return this.variableService.list(pageRequest);
    }

    /**
     * 根据id删除变量
     *
     * @param idRequest 变量id
     * @return true
     */
    @DataPermission(id = "#idRequest.id", dataType = DataType.VARIABLE, operationType = OperationType.DELETE)
    @PostMapping("delete")
    @ApiOperation("根据id删除变量")
    public PlainResult<Boolean> delete(@RequestBody @Valid IdRequest idRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(variableService.delete(idRequest.getId()));
        return plainResult;
    }

    /**
     * 变量名称是否存在
     *
     * @param verifyVariableNameRequest 变量名称
     * @return true存在
     */
    @PostMapping("nameIsExists")
    @ApiOperation("变量名称是否存在")
    public PlainResult<Boolean> nameIsExists(@RequestBody @Valid VerifyVariableNameRequest verifyVariableNameRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(variableService.varNameIsExists(verifyVariableNameRequest));
        return plainResult;
    }

}
