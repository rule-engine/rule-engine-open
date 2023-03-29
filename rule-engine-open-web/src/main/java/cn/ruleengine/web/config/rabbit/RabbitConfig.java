package cn.ruleengine.web.config.rabbit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

/**
 * 〈一句话功能简述〉<br>
 * 〈消息队列配置〉
 *
 * @author 丁乾文
 * @date 2021/6/17
 * @since 1.0.0
 */
@Slf4j
@Component
public class RabbitConfig {


    /**
     * 当配置了rabbitmq配置时启用
     */
    @ConditionalOnProperty("spring.rabbitmq.host")
    @EnableRabbit
    @Component
    @Import(RabbitAutoConfiguration.class)
    public static class RabbitConfiguration {

        public RabbitConfiguration() {
            log.info("load rabbit");
        }


        /**
         * 使用json传输,即使没有实现序列化接口也可以
         *
         * @return MessageConverter
         */
        @Bean
        public MessageConverter messageConverter() {
            return new Jackson2JsonMessageConverter();
        }

    }


    @ConditionalOnMissingBean(RabbitConfiguration.class)
    @Component
    public static class RabbitNoConfigurationWarning {

        /**
         * 未配置Rabbit将导致规则不能正常发布
         */
        public RabbitNoConfigurationWarning() {
            if (log.isWarnEnabled()) {
                log.warn("If Rabbit is not configured, the rules will not be published normally");
            }
        }

    }

}
