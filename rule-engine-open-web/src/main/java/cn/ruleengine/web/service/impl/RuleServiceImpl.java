package cn.ruleengine.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.ruleengine.web.enums.ErrorCodeEnum;
import cn.ruleengine.web.exception.ApiException;
import cn.ruleengine.web.service.ConditionGroupService;
import cn.ruleengine.web.service.RuleService;
import cn.ruleengine.web.store.entity.RuleEngineRule;
import cn.ruleengine.web.store.manager.RuleEngineRuleManager;
import cn.ruleengine.web.store.mapper.RuleEngineRuleMapper;
import cn.ruleengine.web.vo.condition.ConditionGroupConfig;
import cn.ruleengine.web.vo.condition.ConfigValue;
import cn.ruleengine.web.vo.rule.general.SaveActionRequest;
import cn.ruleengine.web.vo.rule.RuleBody;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 〈RuleServiceImpl〉
 *
 * @author 丁乾文
 * @date 2021/7/28 1:08 下午
 * @since 1.0.0
 */
@Service
public class RuleServiceImpl implements RuleService {

    @Resource
    private RuleEngineRuleManager ruleEngineRuleManager;
    @Resource
    private ConditionGroupService conditionGroupService;
    @Resource
    private RuleEngineRuleMapper ruleEngineRuleMapper;

    /**
     * 保存结果
     *
     * @param saveActionRequest 保存结果
     * @return 保存结果
     */
    @Override
    public Boolean saveAction(SaveActionRequest saveActionRequest) {
        Integer ruleId = saveActionRequest.getRuleId();
        ConfigValue configValue = saveActionRequest.getConfigValue();
        RuleEngineRule ruleEngineRule = this.ruleEngineRuleManager.getById(ruleId);
        if (ruleEngineRule == null) {
            throw new ApiException(ErrorCodeEnum.RULE9999404.getCode(), "不存在规则:{}", ruleId);
        }
        ruleEngineRule.setId(ruleId);
        ruleEngineRule.setActionType(configValue.getType());
        ruleEngineRule.setActionValueType(configValue.getValueType());
        ruleEngineRule.setActionValue(configValue.getValue());
        this.ruleEngineRuleMapper.updateRuleById(ruleEngineRule);
        return true;
    }


    /**
     * 保存规则并返回规则id
     *
     * @param ruleBody 规则体
     * @return 规则id
     */
    @Override
    public Integer saveOrUpdateRule(RuleBody ruleBody) {
        RuleEngineRule ruleEngineRule = new RuleEngineRule();
        ruleEngineRule.setId(ruleBody.getId());
        ruleEngineRule.setName(ruleBody.getName());
        ConfigValue action = ruleBody.getAction();
        if (action != null) {
            ruleEngineRule.setActionType(action.getType());
            ruleEngineRule.setActionValueType(action.getValueType());
            ruleEngineRule.setActionValue(action.getValue());
        }
        this.ruleEngineRuleManager.saveOrUpdate(ruleEngineRule);
        List<ConditionGroupConfig> conditionGroup = ruleBody.getConditionGroup();
        if (CollUtil.isNotEmpty(conditionGroup)) {
            this.conditionGroupService.saveConditionGroup(ruleEngineRule.getId(), conditionGroup);
        }
        return ruleEngineRule.getId();
    }


}
