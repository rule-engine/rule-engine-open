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
package cn.ruleengine.core;


import cn.ruleengine.core.exception.EngineException;
import cn.ruleengine.core.listener.ExecuteListener;
import cn.ruleengine.core.rule.GeneralRule;
import cn.ruleengine.core.rule.Rule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

import java.util.Objects;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 * 把规则引擎执行流程反抽象为写程序
 * 程序设计到变量，以及等等数据方法
 *
 * @author dingqianwen
 * @date 2020/3/2
 * @since 1.0.0
 */
@Slf4j
public class GeneralRuleEngine extends Engine {

    /**
     * 引擎中的规则
     */
    private final Container.Body<GeneralRule> generalRuleContainer;

    /**
     * 可传入配置信息，包括规则监听器，规则变量...
     *
     * @param configuration 规则引擎运行所需配置参数
     */
    public GeneralRuleEngine(@NonNull RuleEngineConfiguration configuration) {
        super(configuration);
        this.generalRuleContainer = configuration.getGeneralRuleContainer();
    }


    /**
     * 规则执行，当条件全部成立时，返回规则执行结果{@link Rule#getActionValue()} ()}
     * 否则查看是否存在默认结果，存在返回默认结果{@link GeneralRule#getDefaultActionValue()}，否则返回null
     *
     * @param ruleCode 规则Code
     * @return 规则执行结果
     */
    @Override
    public Output execute(@NonNull Input input, @NonNull String workspaceCode, @NonNull String ruleCode) {
        Objects.requireNonNull(input);
        Objects.requireNonNull(workspaceCode);
        Objects.requireNonNull(ruleCode);
        GeneralRule generalRule = this.generalRuleContainer.get(workspaceCode, ruleCode);
        if (generalRule == null) {
            throw new EngineException("no general rule:{}", ruleCode);
        }
        if (log.isDebugEnabled()) {
            log.debug("开始执行规则:" + generalRule.getCode());
        }
        ExecuteListener<GeneralRule> listener = this.getConfiguration().getGeneralRuleListener();
        listener.before(generalRule, input);
        try {
            Object action = generalRule.execute(input, this.getConfiguration());
            if (log.isDebugEnabled()) {
                log.debug("规则执行完毕:{},{}", generalRule.getCode(), action);
            }
            Output output = new DefaultOutput(action);
            listener.after(generalRule, input, output);
            return output;
        } catch (Exception exception) {
            listener.onException(generalRule, input, exception);
            throw exception;
        }
    }


    /**
     * 销毁规则引擎
     */
    @Override
    public void close() {
        this.generalRuleContainer.close();
        log.info("The general rule engine has been destroyed");
    }

}
