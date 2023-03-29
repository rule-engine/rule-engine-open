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
import cn.ruleengine.web.service.FunctionService;
import cn.ruleengine.web.vo.function.GetFunctionResponse;
import cn.ruleengine.web.vo.function.ListFunctionRequest;
import cn.ruleengine.web.vo.function.ListFunctionResponse;
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
 * @date 2020/8/24
 * @since 1.0.0
 */
@Api(tags = "函数控制器")
@RestController
@RequestMapping("ruleEngine/function")
public class FunctionController {

    @Resource
    private FunctionService functionService;

    /**
     * 函数列表
     *
     * @param pageRequest param
     * @return list
     */
    @PostMapping("list")
    @ApiOperation("函数列表")
    public PageResult<ListFunctionResponse> list(@RequestBody PageRequest<ListFunctionRequest> pageRequest) {
        return functionService.list(pageRequest);
    }

    /**
     * 查询函数详情
     *
     * @param idRequest 函数id
     * @return 函数信息
     */
    @PostMapping("get")
    @ApiOperation("查询函数详情")
    public PlainResult<GetFunctionResponse> get(@RequestBody @Valid IdRequest idRequest) {
        PlainResult<GetFunctionResponse> plainResult = new PlainResult<>();
        plainResult.setData(functionService.get(idRequest.getId()));
        return plainResult;
    }


}
