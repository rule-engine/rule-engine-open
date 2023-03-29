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
package cn.ruleengine.compute.controller;


import cn.ruleengine.common.vo.PlainResult;
import cn.ruleengine.compute.service.FunctionService;
import cn.ruleengine.compute.vo.ExecuteFunctionRequest;
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
     * 函数模拟测试
     *
     * @param executeTestRequest 函数入参值
     * @return result
     */
    @PostMapping("run")
    @ApiOperation("函数模拟测试")
    public PlainResult<Object> run(@Valid @RequestBody ExecuteFunctionRequest executeTestRequest) {
        PlainResult<Object> plainResult = new PlainResult<>();
        plainResult.setData(functionService.run(executeTestRequest));
        return plainResult;
    }


}
