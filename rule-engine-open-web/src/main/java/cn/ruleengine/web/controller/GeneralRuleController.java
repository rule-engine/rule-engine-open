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
import cn.ruleengine.web.annotation.DataPermission;
import cn.ruleengine.web.annotation.ReSubmitLock;
import cn.ruleengine.web.annotation.SystemLog;
import cn.ruleengine.web.enums.DataType;
import cn.ruleengine.web.enums.OperationType;
import cn.ruleengine.web.service.GeneralRuleService;
import cn.ruleengine.web.vo.common.DownloadListResponse;
import cn.ruleengine.web.vo.common.GoBackRequest;
import cn.ruleengine.web.vo.common.HistoryListResponse;
import cn.ruleengine.web.vo.common.ViewRequest;
import cn.ruleengine.web.vo.generalrule.*;
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
@Api(tags = "普通规则控制器")
@RestController
@RequestMapping("ruleEngine/generalRule")
public class GeneralRuleController {

    @Resource
    private GeneralRuleService generalRuleService;

    /**
     * 获取规则列表
     *
     * @param pageRequest 分页查询参数
     * @return page
     */
    @PostMapping("list")
    @ApiOperation("规则列表")
    public PageResult<ListGeneralRuleResponse> list(@RequestBody PageRequest<ListGeneralRuleRequest> pageRequest) {
        return this.generalRuleService.list(pageRequest);
    }

    /**
     * 保存规则定义信息
     *
     * @param ruleDefinition 规则定义信息
     * @return 规则id
     */
    @ReSubmitLock
    @DataPermission(id = "#ruleDefinition.id", dataType = DataType.GENERAL_RULE, operationType = OperationType.ADD)
    @PostMapping("saveRuleDefinition")
    @ApiOperation("保存规则定义信息")
    public BaseResult saveRuleDefinition(@Valid @RequestBody GeneralRuleDefinition ruleDefinition) {
        PlainResult<Integer> plainResult = new PlainResult<>();
        plainResult.setData(generalRuleService.saveRuleDefinition(ruleDefinition));
        return plainResult;
    }

    /**
     * 更新规则定义信息
     *
     * @param ruleDefinition 规则定义信息
     * @return 规则id
     */
    @ReSubmitLock
    @DataPermission(id = "#ruleDefinition.id", dataType = DataType.GENERAL_RULE, operationType = OperationType.UPDATE)
    @PostMapping("updateRuleDefinition")
    @ApiOperation("更新规则定义信息")
    public BaseResult updateRuleDefinition(@Valid @RequestBody GeneralRuleDefinition ruleDefinition) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(generalRuleService.updateRuleDefinition(ruleDefinition));
        return plainResult;
    }

    /**
     * 获取规则定义信息
     *
     * @param idRequest 规则id
     * @return 规则定义信息
     */
    @DataPermission(id = "#idRequest.id", dataType = DataType.GENERAL_RULE, operationType = OperationType.SELECT)
    @PostMapping("getRuleDefinition")
    @ApiOperation("查询规则定义信息")
    public BaseResult getRuleDefinition(@Valid @RequestBody IdRequest idRequest) {
        PlainResult<GeneralRuleDefinition> plainResult = new PlainResult<>();
        plainResult.setData(generalRuleService.getRuleDefinition(idRequest.getId()));
        return plainResult;
    }


    /**
     * 生成测试版本，更新规则数据
     *
     * @param generalRuleBody 规则配置数据
     * @return true
     */
    @ReSubmitLock
    @DataPermission(id = "#generalRuleBody.id", dataType = DataType.GENERAL_RULE, operationType = OperationType.UPDATE)
    @PostMapping("generationRelease")
    @ApiOperation("生成普通规则测试版本")
    public BaseResult generationRelease(@Valid @RequestBody GeneralRuleBody generalRuleBody) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(generalRuleService.generationRelease(generalRuleBody));
        return plainResult;
    }

    /**
     * 规则发布
     *
     * @param idRequest 规则id
     * @return true
     */
    @ReSubmitLock
    @SystemLog(tag = "普通规则发布")
    @DataPermission(id = "#idRequest.id", dataType = DataType.GENERAL_RULE, operationType = OperationType.PUBLISH)
    @PostMapping("publish")
    @ApiOperation("发布规则")
    public BaseResult publish(@Valid @RequestBody IdRequest idRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(generalRuleService.publish(idRequest.getId()));
        return plainResult;
    }

    /**
     * 获取规则信息
     *
     * @param idRequest 规则id
     * @return 规则信息
     */
    @DataPermission(id = "#idRequest.id", dataType = DataType.GENERAL_RULE, operationType = OperationType.SELECT)
    @PostMapping("getRuleConfig")
    @ApiOperation("获取规则配置信息")
    public BaseResult getRuleConfig(@Valid @RequestBody IdRequest idRequest) {
        PlainResult<GetGeneralRuleResponse> plainResult = new PlainResult<>();
        plainResult.setData(generalRuleService.getRuleConfig(idRequest.getId()));
        return plainResult;
    }

    /**
     * 规则预览
     *
     * @param viewRequest 规则id
     * @return GetRuleResponse
     */
    @DataPermission(id = "#viewRequest.id", dataType = DataType.GENERAL_RULE, operationType = OperationType.SELECT)
    @PostMapping("view")
    @ApiOperation("预览规则")
    public BaseResult view(@Valid @RequestBody ViewRequest viewRequest) {
        PlainResult<ViewGeneralRuleResponse> plainResult = new PlainResult<>();
        plainResult.setData(generalRuleService.view(viewRequest));
        return plainResult;
    }


    /**
     * 删除规则
     *
     * @param idRequest 规则id
     * @return true
     */
    @DataPermission(id = "#idRequest.id", dataType = DataType.GENERAL_RULE, operationType = OperationType.DELETE)
    @PostMapping("delete")
    @ApiOperation("删除规则")
    public BaseResult delete(@Valid @RequestBody IdRequest idRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(generalRuleService.delete(idRequest.getId()));
        return plainResult;
    }

    /**
     * 规则code是否存在
     *
     * @param param 规则code
     * @return true存在
     */
    @PostMapping("codeIsExists")
    @ApiOperation("规则Code是否存在")
    public PlainResult<Boolean> codeIsExists(@RequestBody @Valid Param<String> param) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(generalRuleService.ruleCodeIsExists(param.getParam()));
        return plainResult;
    }

    /**
     * 保存默认结果
     *
     * @param saveActionRequest 保存默认结果
     * @return true
     */
    @PostMapping("saveDefaultAction")
    @ApiOperation("保存默认结果")
    public PlainResult<Boolean> saveDefaultAction(@RequestBody @Valid SaveDefaultActionRequest saveActionRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(generalRuleService.saveDefaultAction(saveActionRequest));
        return plainResult;
    }

    /**
     * 是否开启默认结果
     *
     * @param defaultActionSwitchRequest 是否开启默认结果
     * @return true
     */
    @PostMapping("defaultActionSwitch")
    @ApiOperation("是否开启默认结果")
    public PlainResult<Boolean> defaultActionSwitch(@RequestBody @Valid DefaultActionSwitchRequest defaultActionSwitchRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(generalRuleService.defaultActionSwitch(defaultActionSwitchRequest));
        return plainResult;
    }

    /**
     * 可引用的规则列表
     *
     * @param publishListRequest p
     * @return r
     */
    @PostMapping("referenceableList")
    @ApiOperation("可引用的规则列表")
    public PageResult<PublishListResponse> referenceableList(@RequestBody PageRequest<PublishListRequest> publishListRequest) {
        return generalRuleService.referenceableList(publishListRequest);
    }

    /**
     * 规则下载列表
     *
     * @param pageRequest 规则id
     * @return true
     */
    @PostMapping("downloadList")
    @ApiOperation("规则下载列表")
    public PageResult<DownloadListResponse> downloadList(@RequestBody @Valid PageRequest<IdRequest> pageRequest) {
        return this.generalRuleService.downloadList(pageRequest);
    }


    /**
     * 规则历史列表
     *
     * @param pageRequest 规则id
     * @return true
     */
    @PostMapping("historyList")
    @ApiOperation("规则历史列表")
    public PageResult<HistoryListResponse> historyList(@RequestBody @Valid PageRequest<IdRequest> pageRequest) {
        return this.generalRuleService.historyList(pageRequest);
    }


    /**
     * 规则回退
     *
     * @param goBackRequest 回退版本信息
     * @return true
     */
    @DataPermission(id = "#goBackRequest.dataId", dataType = DataType.GENERAL_RULE, operationType = OperationType.UPDATE)
    @PostMapping("goBack")
    @ApiOperation("回退")
    public PlainResult<Boolean> goBack(@RequestBody @Valid GoBackRequest goBackRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(generalRuleService.goBack(goBackRequest));
        return plainResult;
    }


    /**
     * 删除历史规则
     *
     * @param idRequest id
     * @return true
     */
    @PostMapping("deleteHistoricalRules")
    @ApiOperation("删除历史规则")
    public BaseResult deleteHistoricalRules(@RequestBody @Valid IdRequest idRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(generalRuleService.deleteHistoricalRules(idRequest.getId()));
        return plainResult;

    }

}
