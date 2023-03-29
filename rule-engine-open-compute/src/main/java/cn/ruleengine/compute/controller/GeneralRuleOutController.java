/**
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


import cn.ruleengine.common.vo.BaseResult;
import cn.ruleengine.common.vo.PlainResult;
import cn.ruleengine.compute.service.RuleEngineOutService;
import cn.ruleengine.compute.vo.BatchExecuteRequest;
import cn.ruleengine.compute.vo.ExecuteRequest;
import cn.ruleengine.compute.vo.IsExistsRequest;
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
 * @date 2020/7/16
 * @since 1.0.0
 */
@Api(tags = "调用普通规则控制器")
@RestController
@RequestMapping("ruleEngine/generalRule")
public class GeneralRuleOutController {


    @Resource
    private RuleEngineOutService ruleOutService;

    /**
     * 执行单个规则，获取执行结果
     *
     * @param executeRuleRequest 执行规则入参
     * @return 规则执行结果
     */
    @PostMapping("execute")
    @ApiOperation("执行单个规则，获取执行结果")
    public BaseResult executeRule(@RequestBody @Valid ExecuteRequest executeRuleRequest) {
        PlainResult<Object> plainResult = new PlainResult<>();
        plainResult.setData(ruleOutService.execute(executeRuleRequest));
        return plainResult;
    }

    /**
     * 批量执行多个规则(一次建议最多1000个)，获取执行结果
     *
     * @param batchExecuteRuleRequest 执行规则入参
     * @return 规则执行结果
     */
    @PostMapping("batchExecute")
    @ApiOperation("批量执行多个规则，获取执行结果")
    public BaseResult batchExecuteRule(@RequestBody @Valid BatchExecuteRequest batchExecuteRuleRequest) {
        PlainResult<Object> plainResult = new PlainResult<>();
        plainResult.setData(ruleOutService.batchExecute(batchExecuteRuleRequest));
        return plainResult;
    }

    /**
     * 引擎中是否存在这个规则
     *
     * @param isExistsRuleRequest 参数
     * @return true存在
     */
    @PostMapping("isExists")
    @ApiOperation("引擎中是否存在这个规则")
    public BaseResult isExists(@RequestBody @Valid IsExistsRequest isExistsRuleRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(ruleOutService.isExists(isExistsRuleRequest));
        return plainResult;
    }
}

