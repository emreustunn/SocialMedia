package com.bilgeadam.config.rabbitmq;

import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    @Value("${rabbitmq.queueRegister}")
    private String queueNameRegister;
    @Value("${rabbitmq.queueregisterelastic}")
    private String elasticRegisterQueue;
    @Value("${rabbitmq.elasticregisterkey}")
    private String elasticRegisterBindingKey;
    @Value("${rabbitmq.exchange-user}")
    private String exchange;

    @Bean
    Queue registerQueue(){
        return new Queue(queueNameRegister);
    }

    @Bean
    Queue registerQueueElastic(){
        return new Queue(elasticRegisterQueue);
    }

    @Bean
    DirectExchange exchangeUser(){
        return new DirectExchange(exchange);
    }

    @Bean
    Binding bindingRegisterElastic(final Queue registerQueueElastic,DirectExchange exchangeUser){
        return BindingBuilder.bind(registerQueueElastic).to(exchangeUser).with(elasticRegisterBindingKey);
    }


}
