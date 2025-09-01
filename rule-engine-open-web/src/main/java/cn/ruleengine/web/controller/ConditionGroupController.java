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


import cn.ruleengine.common.vo.BaseResult;
import cn.ruleengine.common.vo.IdRequest;
import cn.ruleengine.common.vo.PlainResult;
import cn.ruleengine.web.annotation.ReSubmitLock;
import cn.ruleengine.web.service.ConditionGroupService;
import cn.ruleengine.web.vo.common.RearrangeRequest;
import cn.ruleengine.web.vo.condition.group.DeleteConditionGroupRequest;
import cn.ruleengine.web.vo.condition.group.SaveOrUpdateConditionGroup;
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
@Api(tags = "条件组控制器")
@RestController
@RequestMapping("ruleEngine/conditionGroup")
public class ConditionGroupController {

    @Resource
    private ConditionGroupService ruleEngineConditionGroupService;

    /**
     * 保存或者更新条件组
     *
     * @param saveOrUpdateConditionGroup 条件组信息
     * @return 体检组id
     */
    @ReSubmitLock
    @PostMapping("saveOrUpdate")
    @ApiOperation("保存或者更新条件组")
    public PlainResult<Integer> saveOrUpdateConditionGroup(@RequestBody @Valid SaveOrUpdateConditionGroup saveOrUpdateConditionGroup) {
        PlainResult<Integer> plainResult = new PlainResult<>();
        plainResult.setData(ruleEngineConditionGroupService.saveOrUpdateConditionGroup(saveOrUpdateConditionGroup));
        return plainResult;
    }

    /**
     * 删除条件组
     *
     * @param deleteConditionGroupRequest 条件组id
     * @return true：删除成功
     */
    @PostMapping("delete")
    @ApiOperation("根据id删除条件组")
    public PlainResult<Boolean> delete(@RequestBody @Valid DeleteConditionGroupRequest deleteConditionGroupRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(ruleEngineConditionGroupService.delete(deleteConditionGroupRequest.getId()));
        return plainResult;
    }

    /**
     * 重新排序
     *
     * @param rearrangeRequests r
     * @return true
     */
    @PostMapping("rearrange")
    @ApiOperation("重新排序")
    public PlainResult<Boolean> rearrange(@RequestBody @Valid List<RearrangeRequest> rearrangeRequests) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(ruleEngineConditionGroupService.rearrange(rearrangeRequests));
        return plainResult;
    }

}
