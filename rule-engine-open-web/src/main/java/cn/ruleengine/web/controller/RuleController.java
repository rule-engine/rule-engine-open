package cn.ruleengine.web.controller;

import cn.ruleengine.common.vo.BaseResult;
import cn.ruleengine.common.vo.PlainResult;
import cn.ruleengine.web.service.RuleService;
import cn.ruleengine.web.vo.generalrule.SaveActionRequest;
import cn.ruleengine.web.vo.rule.RuleBody;
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
 * 〈RuleController〉
 *
 * @author 丁乾文
 * @date 2021/7/28 1:02 下午
 * @since 1.0.0
 */
@Api(tags = "规则控制器")
@RestController
@RequestMapping("ruleEngine/rule")
public class RuleController {


    @Resource
    private RuleService ruleService;


    /**
     * 保存规则并返回规则id
     *
     * @param ruleBody 规则体
     * @return 规则id
     */
    @ApiOperation("保存或者更新规则")
    @PostMapping("saveOrUpdateRule")
    public BaseResult saveOrUpdateRule(@RequestBody RuleBody ruleBody) {
        PlainResult<Integer> plainResult = new PlainResult<>();
        plainResult.setData(ruleService.saveOrUpdateRule(ruleBody));
        return plainResult;
    }

    /**
     * 保存结果
     *
     * @param saveActionRequest 保存结果
     * @return 保存结果
     */
    @PostMapping("saveAction")
    @ApiOperation("保存结果")
    public PlainResult<Boolean> saveAction(@RequestBody @Valid SaveActionRequest saveActionRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(ruleService.saveAction(saveActionRequest));
        return plainResult;
    }


}
