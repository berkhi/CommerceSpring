package com.CommerceSpring.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    private final String commerceSpringDirectExchange = "commerceSpringDirectExchange";

    private final String queueSaveUserFromAuth = "queueSaveUserFromAuth";
    private final String keySaveUserFromAuth = "keySaveUserFromAuth";

    private final String queueSendVerificationEmail = "queueSendVerificationEmail";
    private final String keySendVerificationEmail = "keySendVerificationEmail";



    @Bean
    public DirectExchange commerceSpringDirectExchange() {return new DirectExchange(commerceSpringDirectExchange);}



    @Bean
    public Queue queueSaveUserFromAuth(){
        return new Queue(queueSaveUserFromAuth);
    }
    @Bean
    public Queue queueSendVerificationEmail(){
        return new Queue(queueSendVerificationEmail);
    }



    @Bean
    public Binding bindingSaveUserFromAuth(){
        return BindingBuilder.bind(queueSaveUserFromAuth()).to(commerceSpringDirectExchange()).with(keySaveUserFromAuth);
    }
    @Bean
    public Binding bindingSendVerificationEmail(){
        return BindingBuilder.bind(queueSendVerificationEmail()).to(commerceSpringDirectExchange()).with(keySendVerificationEmail);
    }



    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }

    @Bean
    MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

}
