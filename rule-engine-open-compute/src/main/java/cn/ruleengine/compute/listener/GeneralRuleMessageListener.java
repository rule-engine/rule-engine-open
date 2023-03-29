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
package cn.ruleengine.compute.listener;

import cn.ruleengine.compute.config.rabbit.RabbitTopicConfig;
import cn.ruleengine.compute.listener.body.GeneralRuleMessageBody;
import cn.ruleengine.compute.service.GeneralRulePublishService;
import cn.ruleengine.core.Container;
import cn.ruleengine.core.RuleEngineConfiguration;
import cn.ruleengine.core.rule.GeneralRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/7/16
 * @since 1.0.0
 */
@Slf4j
@Component
public class GeneralRuleMessageListener {

    @Resource
    private RuleEngineConfiguration ruleEngineConfiguration;
    @Resource
    private GeneralRulePublishService rulePublishService;


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(),
            exchange = @Exchange(value = RabbitTopicConfig.RULE_EXCHANGE, type = ExchangeTypes.TOPIC),
            key = RabbitTopicConfig.RULE_TOPIC_ROUTING_KEY)
    )
    public void message(GeneralRuleMessageBody ruleMessageBody) {
        log.info("规则消息：{}", ruleMessageBody);
        String workspaceCode = ruleMessageBody.getWorkspaceCode();
        String ruleCode = ruleMessageBody.getRuleCode();
        Container.Body<GeneralRule> generalRuleContainer = this.ruleEngineConfiguration.getGeneralRuleContainer();
        switch (ruleMessageBody.getType()) {
            case UPDATE:
                log.info("开始更新规则：{}", ruleCode);
                generalRuleContainer.add(rulePublishService.getPublishGeneralRule(workspaceCode, ruleCode));
                log.info("更新完毕：{}", ruleCode);
                break;
            case LOAD:
                log.info("开始加载规则：{}", ruleCode);
                generalRuleContainer.add(rulePublishService.getPublishGeneralRule(workspaceCode, ruleCode));
                log.info("加载完毕：{}", ruleCode);
                break;
            case REMOVE:
                log.info("开始移除规则：{}", ruleCode);
                generalRuleContainer.remove(workspaceCode, ruleCode);
                log.info("移除完毕：{}", ruleCode);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + ruleMessageBody.getType());
        }
    }
}
