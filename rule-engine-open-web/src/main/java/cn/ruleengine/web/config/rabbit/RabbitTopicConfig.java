package cn.ruleengine.web.config.rabbit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 〈一句话功能简述〉<br>
 * 〈发布与订阅〉
 *
 * @author 丁乾文
 * @date 2021/6/17
 * @since 1.0.0
 */
@Slf4j
@ConditionalOnBean(RabbitConfig.class)
@Component
public class RabbitTopicConfig {

    private static final String RULE_TOPIC = "boot_engine_rule_topic";
    public static final String RULE_EXCHANGE = "boot_engine_rule_exchange";
    final public static String RULE_TOPIC_ROUTING_KEY = "boot_engine_rule_topic_routingKey";

    private static final String RULE_SET_TOPIC = "boot_engine_rule_set_topic";
    public static final String RULE_SET_EXCHANGE = "boot_engine_rule_set_exchange";
    final public static String RULE_SET_TOPIC_ROUTING_KEY = "boot_engine_rule_set_topic_routingKey";

    private static final String DECISION_TABLE_TOPIC = "boot_engine_decision_table_topic";
    public static final String DECISION_TABLE_EXCHANGE = "boot_engine_decision_table_exchange";
    final public static String DECISION_TABLE_TOPIC_ROUTING_KEY = "boot_engine_decision_table_topic_routingKey";

    private static final String VAR_TOPIC = "boot_engine_var_topic";
    public static final String VAR_EXCHANGE = "boot_engine_var_exchange";
    final public static String VAR_TOPIC_ROUTING_KEY = "boot_engine_var_topic_routingKey";


    @Bean
    public TopicExchange ruleExchange() {
        return new TopicExchange(RULE_EXCHANGE);
    }

    @Bean
    public Queue ruleQueue() {
        return new Queue(RULE_TOPIC);
    }

    @Bean
    public Binding ruleBindingExchangeMessage(@Qualifier("ruleQueue") Queue ruleQueue,
                                              @Qualifier("ruleExchange") TopicExchange ruleExchange) {
        return BindingBuilder.bind(ruleQueue).to(ruleExchange).with(RULE_TOPIC_ROUTING_KEY);
    }

    @Bean
    public TopicExchange ruleSetExchange() {
        return new TopicExchange(RULE_SET_EXCHANGE);
    }

    @Bean
    public Queue ruleSetQueue() {
        return new Queue(RULE_SET_TOPIC);
    }

    @Bean
    public Binding ruleSetBindingExchangeMessage(@Qualifier("ruleSetQueue") Queue ruleSetQueue,
                                                 @Qualifier("ruleSetExchange") TopicExchange ruleSetExchange) {
        return BindingBuilder.bind(ruleSetQueue).to(ruleSetExchange).with(RULE_SET_TOPIC_ROUTING_KEY);
    }


    @Bean
    public TopicExchange decisionTableExchange() {
        return new TopicExchange(DECISION_TABLE_EXCHANGE);
    }

    @Bean
    public Queue decisionTableQueue() {
        return new Queue(DECISION_TABLE_TOPIC);
    }

    @Bean
    public Binding decisionTableBindingExchangeMessage(@Qualifier("decisionTableQueue") Queue decisionTableQueue,
                                                       @Qualifier("decisionTableExchange") TopicExchange decisionTableExchange) {
        return BindingBuilder.bind(decisionTableQueue).to(decisionTableExchange).with(DECISION_TABLE_TOPIC_ROUTING_KEY);
    }


    @Bean
    public TopicExchange varExchange() {
        return new TopicExchange(VAR_EXCHANGE);
    }

    @Bean
    public Queue varQueue() {
        return new Queue(VAR_TOPIC);
    }

    @Bean
    public Binding varBindingExchangeMessage(@Qualifier("varQueue") Queue varQueue,
                                             @Qualifier("varExchange") TopicExchange varExchange) {
        return BindingBuilder.bind(varQueue).to(varExchange).with(VAR_TOPIC_ROUTING_KEY);
    }
}
