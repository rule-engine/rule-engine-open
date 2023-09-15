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
import cn.ruleengine.web.service.InputParameterService;
import cn.ruleengine.web.vo.parameter.*;
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
@Api(tags = "规则参数控制器")
@RestController
@RequestMapping("ruleEngine/inputParameter")
public class InputParameterController {

    @Resource
    private InputParameterService inputParameterService;

    /**
     * 添加规则参数
     *
     * @param addConditionRequest 规则参数信息
     * @return true
     */
    @ReSubmitLock
    @PostMapping("add")
    @ApiOperation("添加规则参数")
    public PlainResult<Boolean> add(@RequestBody @Valid AddInputParameterRequest addConditionRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(inputParameterService.add(addConditionRequest));
        return plainResult;
    }

    /**
     * 规则参数列表
     *
     * @param pageRequest param
     * @return r
     */
    @PostMapping("list")
    @ApiOperation("规则参数列表")
    public PageResult<ListInputParameterResponse> list(@RequestBody @Valid PageRequest<ListInputParameterRequest> pageRequest) {
        return this.inputParameterService.list(pageRequest);
    }

    /**
     * 根据id查询规则参数
     *
     * @param idRequest 规则参数id
     * @return r
     */
    @DataPermission(id = "#idRequest.id", dataType = DataType.INPUT_PARAMETER, operationType = OperationType.SELECT)
    @PostMapping("get")
    @ApiOperation("根据id查询规则参数")
    public PlainResult<GetInputParameterResponse> get(@RequestBody @Valid IdRequest idRequest) {
        PlainResult<GetInputParameterResponse> plainResult = new PlainResult<>();
        plainResult.setData(inputParameterService.get(idRequest.getId()));
        return plainResult;
    }

    /**
     * 根据规则参数id更新规则参数
     *
     * @param inputParameterRequest 规则参数信息
     * @return true
     */
    @ReSubmitLock
    @DataPermission(id = "#inputParameterRequest.id", dataType = DataType.INPUT_PARAMETER, operationType = OperationType.UPDATE)
    @PostMapping("update")
    @ApiOperation("根据id更新规则参数")
    public PlainResult<Boolean> update(@RequestBody @Valid UpdateInputParameterRequest inputParameterRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(inputParameterService.update(inputParameterRequest));
        return plainResult;
    }

    /**
     * 根据id删除规则参数
     *
     * @param idRequest 规则参数id
     * @return true
     */
    @DataPermission(id = "#idRequest.id", dataType = DataType.INPUT_PARAMETER, operationType = OperationType.DELETE)
    @PostMapping("delete")
    @ApiOperation("根据id删除规则参数")
    public PlainResult<Boolean> delete(@RequestBody @Valid IdRequest idRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(inputParameterService.delete(idRequest.getId()));
        return plainResult;
    }

    /**
     * 规则参数code是否存在
     *
     * @param verifyInputParameterCodeRequest 规则参数code
     * @return true存在
     */
    @PostMapping("codeIsExists")
    @ApiOperation("规则参数code是否存在")
    public PlainResult<Boolean> codeIsExists(@RequestBody @Valid VerifyInputParameterCodeRequest verifyInputParameterCodeRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(inputParameterService.codeIsExists(verifyInputParameterCodeRequest));
        return plainResult;
    }

}
