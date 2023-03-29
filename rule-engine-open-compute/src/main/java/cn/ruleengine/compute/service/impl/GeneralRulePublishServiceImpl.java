package cn.ruleengine.compute.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.ruleengine.compute.enums.DataStatus;
import cn.ruleengine.compute.service.GeneralRulePublishService;
import cn.ruleengine.compute.store.entity.RuleEngineGeneralRulePublish;
import cn.ruleengine.compute.store.manager.RuleEngineGeneralRulePublishManager;
import cn.ruleengine.core.rule.GeneralRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/9/4
 * @since 1.0.0
 */
@Slf4j
@Service
public class GeneralRulePublishServiceImpl implements GeneralRulePublishService {

    @Resource
    private RuleEngineGeneralRulePublishManager ruleEngineGeneralRulePublishManager;

    /**
     * 根据规则code，查询发布规则
     *
     * @param workspaceCode 工作空间code
     * @param ruleCode      规则code
     * @return 规则
     */
    @Override
    public GeneralRule getPublishGeneralRule(String workspaceCode, String ruleCode) {
        RuleEngineGeneralRulePublish rulePublish = this.ruleEngineGeneralRulePublishManager.lambdaQuery()
                .eq(RuleEngineGeneralRulePublish::getStatus, DataStatus.PRD.getStatus())
                .eq(RuleEngineGeneralRulePublish::getGeneralRuleCode, ruleCode)
                .eq(RuleEngineGeneralRulePublish::getWorkspaceCode, workspaceCode)
                .one();
        return GeneralRule.buildRule(rulePublish.getData());
    }

    /**
     * 获取所有的线上规则
     *
     * @return rule
     */
    @Override
    public List<GeneralRule> getAllPublishGeneralRule() {
        List<RuleEngineGeneralRulePublish> rulePublishList = this.ruleEngineGeneralRulePublishManager.lambdaQuery()
                .eq(RuleEngineGeneralRulePublish::getStatus, DataStatus.PRD.getStatus())
                .list();
        if (CollUtil.isEmpty(rulePublishList)) {
            return Collections.emptyList();
        }
        List<GeneralRule> rules = new ArrayList<>(rulePublishList.size());
        for (RuleEngineGeneralRulePublish publish : rulePublishList) {
            try {
                log.info("parse rule for workspace code: {} rule code: {}", publish.getWorkspaceCode(), publish.getGeneralRuleCode());
                GeneralRule rule = GeneralRule.buildRule(publish.getData());
                rules.add(rule);
            } catch (Exception e) {
                log.error("parse rule error ", e);
            }
        }
        return rules;
    }

}
