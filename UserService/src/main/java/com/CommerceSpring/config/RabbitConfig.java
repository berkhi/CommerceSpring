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

    //Auth'dan register olup otomatik oluşturularacak user'lar için queue ve key
    private final String queueSaveUserFromAuth = "queueSaveUserFromAuth";
    private final String keySaveUserFromAuth = "keySaveUserFromAuth";

    //GelenAuthId ile security için Rolleri gönderme
    private final String queueRolesByAuthId = "queueRolesByAuthId";
    private final String keyRolesByAuthId = "keyRolesByAuthId";

    //AuthId ile auth servisten email ve password isteme (securit için gerekli)
    private final String queueEmailAndPasswordFromAuth = "queueEmailAndPasswordFromAuth";
    private final String keyEmailAndPasswordFromAuth = "keyEmailAndPasswordFromAuth";



    @Bean
    public DirectExchange commerceSpringDirectExchange() {return new DirectExchange(commerceSpringDirectExchange);}



    @Bean
    public Queue queueSaveUserFromAuth() {
        return new Queue(queueSaveUserFromAuth);
    }
    @Bean
    public Queue queueRolesByAuthId() {
        return new Queue(queueRolesByAuthId);
    }
    @Bean
    public Queue queueEmailAndPasswordFromAuth() {
        return new Queue(queueEmailAndPasswordFromAuth);
    }


    @Bean
    public Binding bindingUserSaveFromAuth (Queue queueSaveUserFromAuth, DirectExchange businessDirectExchange) {
        return BindingBuilder.bind(queueSaveUserFromAuth).to(businessDirectExchange).with(keySaveUserFromAuth);
    }
    @Bean
    public Binding bindingRolesByAuthId (Queue queueRolesByAuthId, DirectExchange commerceSpringDirectExchange) {
        return BindingBuilder.bind(queueRolesByAuthId).to(commerceSpringDirectExchange).with(keyRolesByAuthId);
    }
    @Bean
    public Binding bindingEmailAndPasswordFromAuth (Queue queueEmailAndPasswordFromAuth, DirectExchange commerceSpringDirectExchange) {
        return BindingBuilder.bind(queueEmailAndPasswordFromAuth).to(commerceSpringDirectExchange).with(keyEmailAndPasswordFromAuth);
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
