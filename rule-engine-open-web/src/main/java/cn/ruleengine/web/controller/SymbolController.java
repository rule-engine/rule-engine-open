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
package cn.ruleengine.web.controller;


import cn.ruleengine.common.vo.Param;
import cn.ruleengine.common.vo.PlainResult;
import cn.ruleengine.web.service.SymbolService;
import cn.ruleengine.web.vo.symbol.SymbolResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/7/14
 * @since 1.0.0
 */
@Api(tags = "运算符控制器")
@RestController
@RequestMapping("ruleEngine/symbol")
public class SymbolController {

    @Resource
    private SymbolService symbolService;

    /**
     * 获取规则引擎运算符
     *
     * @param param 例如：CONTROLLER
     * @return >,<,=..
     */
    @PostMapping("getByType")
    @ApiOperation("获取规则引擎运算符")
    public PlainResult<List<SymbolResponse>> getByType(@RequestBody @Valid Param<String> param) {
        PlainResult<List<SymbolResponse>> listPlainResult = new PlainResult<>();
        listPlainResult.setData(symbolService.getByType(param.getParam()));
        return listPlainResult;
    }

}
