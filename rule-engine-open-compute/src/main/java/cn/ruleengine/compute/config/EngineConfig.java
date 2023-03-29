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
package cn.ruleengine.compute.config;

import cn.ruleengine.compute.service.GeneralRulePublishService;
import cn.ruleengine.compute.service.VariableResolveService;
import cn.ruleengine.core.*;
import cn.ruleengine.core.cache.DefaultFunctionCache;
import cn.ruleengine.core.listener.ExecuteListener;
import cn.ruleengine.core.rule.GeneralRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/4/7
 * @since 1.0.0
 */
@Slf4j
@Component
public class EngineConfig {

    @Resource
    private VariableResolveService variableResolveService;
    @Resource
    private GeneralRulePublishService rulePublishService;

    /**
     * 规则引擎配置
     *
     * @return RuleEngineConfiguration
     */
    @Bean(destroyMethod = "close")
    public RuleEngineConfiguration ruleEngineConfiguration(Listener.GeneralExecuteListener generalExecuteListener) {
        RuleEngineConfiguration configuration = new RuleEngineConfiguration();
        configuration.getEngineVariable().addMultipleVariable(this.variableResolveService.getAllVariable());
        configuration.setFunctionCache(new DefaultFunctionCache(1000));
        // 普通规则执行监听器
        configuration.setGeneralRuleListener(generalExecuteListener);
        return configuration;
    }

    /**
     * 规则引擎
     *
     * @return engine
     */
    @Primary
    @Bean(destroyMethod = "close")
    public GeneralRuleEngine generalRuleEngine(RuleEngineConfiguration ruleEngineConfiguration) {
        log.info("开始初始化普通规则引擎");
        Container.Body<GeneralRule> generalRuleContainer = ruleEngineConfiguration.getGeneralRuleContainer();
        generalRuleContainer.addMultiple(this.rulePublishService.getAllPublishGeneralRule());
        GeneralRuleEngine ruleEngine = new GeneralRuleEngine(ruleEngineConfiguration);
        log.info("普通规则引擎初始化完毕");
        return ruleEngine;
    }


    @Component
    public static class Listener {

        @Component
        public static class GeneralExecuteListener implements ExecuteListener<GeneralRule> {

            @Override
            public void before(GeneralRule generalRule, Input input) {

            }

            @Override
            public void onException(GeneralRule generalRule, Input input, Exception exception) {

            }

            @Override
            public void after(GeneralRule generalRule, Input input, Output output) {

            }

        }

    }

}
