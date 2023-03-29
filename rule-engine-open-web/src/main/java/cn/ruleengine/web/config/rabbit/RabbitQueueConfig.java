package cn.ruleengine.web.config.rabbit;

import org.springframework.amqp.core.Queue;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 〈一句话功能简述〉<br>
 * 〈队列〉
 *
 * @author 丁乾文
 * @date 2021/6/17
 * @since 1.0.0
 */
@ConditionalOnBean(RabbitConfig.class)
@Component
public class RabbitQueueConfig {

    /**
     * 用于日志的队列
     */
    public final static String SYSTEM_LOG_QUEUE = "boot_engine_system_log_queue";

    /**
     * 用于日志的队列
     *
     * @return Queue
     */
    @Bean
    public Queue systemLogQueue() {
        return new Queue(SYSTEM_LOG_QUEUE, true);
    }

}
