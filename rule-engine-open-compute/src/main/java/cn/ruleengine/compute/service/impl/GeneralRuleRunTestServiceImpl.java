package cn.ruleengine.compute.service.impl;

import cn.ruleengine.compute.enums.DataStatus;
import cn.ruleengine.compute.service.RunTestService;
import cn.ruleengine.compute.store.entity.RuleEngineGeneralRulePublish;
import cn.ruleengine.compute.store.manager.RuleEngineGeneralRulePublishManager;
import cn.ruleengine.compute.vo.RunTestRequest;
import cn.ruleengine.core.*;
import cn.ruleengine.core.exception.ValidException;
import cn.ruleengine.core.rule.GeneralRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/26
 * @since 1.0.0
 */
@Primary
@Slf4j
@Service
public class GeneralRuleRunTestServiceImpl implements RunTestService {

    @Resource
    private RuleEngineConfiguration ruleEngineConfiguration;
    @Resource
    private RuleEngineGeneralRulePublishManager ruleEngineGeneralRulePublishManager;

    /**
     * 规则模拟运行
     *
     * @param runTestRequest 规则参数信息
     * @return result
     */
    @Override
    public Object run(RunTestRequest runTestRequest) {
        log.info("模拟运行规则：{}", runTestRequest.getCode());
        RuleEngineGeneralRulePublish rulePublish = this.ruleEngineGeneralRulePublishManager.lambdaQuery()
                .eq(RuleEngineGeneralRulePublish::getStatus, runTestRequest.getStatus())
                .eq(RuleEngineGeneralRulePublish::getGeneralRuleCode, runTestRequest.getCode())
                .eq(RuleEngineGeneralRulePublish::getWorkspaceCode, runTestRequest.getWorkspaceCode())
                .one();
        if (rulePublish == null) {
            // 如果测试找不到，用线上  此场景出现在只有一个线上的时候
            rulePublish = this.ruleEngineGeneralRulePublishManager.lambdaQuery()
                    .eq(RuleEngineGeneralRulePublish::getStatus, DataStatus.PRD.getStatus())
                    .eq(RuleEngineGeneralRulePublish::getGeneralRuleCode, runTestRequest.getCode())
                    .eq(RuleEngineGeneralRulePublish::getWorkspaceCode, runTestRequest.getWorkspaceCode())
                    .one();
            if (rulePublish == null) {
                throw new ValidException("找不到可运行的规则数据:{},{},{}", runTestRequest.getWorkspaceCode(), runTestRequest.getCode(), runTestRequest.getStatus());
            }
        }
        Input input = new DefaultInput(runTestRequest.getParam());
        log.info("初始化规则引擎");
        RuleEngineConfiguration ruleEngineConfiguration = new RuleEngineConfiguration();
        Container.Body<GeneralRule> generalRuleContainer = ruleEngineConfiguration.getGeneralRuleContainer();
        GeneralRule rule = GeneralRule.buildRule(rulePublish.getData());
        generalRuleContainer.add(rule);
        GeneralRuleEngine engine = new GeneralRuleEngine(ruleEngineConfiguration);
        // 加载变量
        engine.getConfiguration().setEngineVariable(this.ruleEngineConfiguration.getEngineVariable());
        return engine.execute(input, runTestRequest.getWorkspaceCode(), runTestRequest.getCode());
    }

}
