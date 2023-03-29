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
package cn.ruleengine.web.listener;

import cn.ruleengine.web.config.rabbit.RabbitQueueConfig;
import cn.ruleengine.web.store.entity.RuleEngineSystemLog;
import cn.ruleengine.web.store.manager.RuleEngineSystemLogManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @date 2021/6/17
 * @since 1.0.0
 */
@Slf4j
@Component
public class SystemLogMessageListener {

    @Resource
    private RuleEngineSystemLogManager ruleEngineSystemLogManager;

    /**
     * 接收日志消息
     *
     * @param ruleEngineSystemLog 日志内容
     */
    @RabbitListener(queues = RabbitQueueConfig.SYSTEM_LOG_QUEUE)
    public void message(RuleEngineSystemLog ruleEngineSystemLog) {
        log.info("接收到日志消息,准备存入数据库!");
        this.ruleEngineSystemLogManager.save(ruleEngineSystemLog);
    }

}
