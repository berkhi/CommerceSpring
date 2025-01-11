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

    private final String queueSendVerificationEmail = "queueSendVerificationEmail";
    private final String keySendVerificationEmail = "keySendVerificationEmail";

    private final String queueForgetPassword = "queueForgetPassword";
    private final String keyForgetPassword = "keyForgetPassword";

    private final String queueSendMail = "queueSendMail";
    private final String keySendMail = "keySendMail";

    //Admin Tarafından Kullanıcıya yeni şifre göndermek için mailService iletişim kuyruğı
    private static final String queueSendMailNewPassword = "queueSendMailNewPassword";
    private static final String keySendMailNewPassword = "keySendMailNewPassword";

    private final String queueSendStyledEmail = "queueSendStyledEmail";
    private final String keySendStyledEmail = "keySendStyledEmail";

    String queueSaveCustomerSendMail = "queueSaveCustomerSendMail";
    String keySaveCustomerSendMail = "keySaveCustomerSendMail";
    String queueCustomerSendEmailAboutOpportunity = "queueCustomerSendEmailAboutOpportunity";
    String keyCustomerSendEmailAboutOpportunity = "keyCustomerSendEmailAboutOpportunity";

    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(commerceSpringDirectExchange);
    }

    @Bean
    public Queue queueSendVerificationEmail(){return new Queue(queueSendVerificationEmail);}
    @Bean
    public Queue queueSendMail(){
        return new Queue(queueSendMail);
    }
    @Bean
    public Queue queueSendStyledEmail(){
        return new Queue(queueSendStyledEmail);
    }
    @Bean
    public Queue queueForgetPassword() {
        return new Queue(queueForgetPassword);
    }
    @Bean
    public Queue queueSendMailNewPassword() {
        return new Queue(queueSendMailNewPassword);
    }
    @Bean
    public Queue queueSaveCustomerSendMail(){
        return new Queue(queueSaveCustomerSendMail);
    }


    @Bean
    public Binding bindingSendVerificationEmail(){return BindingBuilder.bind(queueSendVerificationEmail()).to(directExchange()).with(keySendVerificationEmail);}
    @Bean
    public Binding bindingKeySendMail(){return BindingBuilder.bind(queueSendMail()).to(directExchange()).with(keySendMail);}
    @Bean
    public Binding bindingKeySendStyledEmail(){return BindingBuilder.bind(queueSendStyledEmail()).to(directExchange()).with(keySendStyledEmail);}
    @Bean
    public Binding bindingForgetPassword (Queue queueForgetPassword, DirectExchange commerceSpringDirectExchange) {return BindingBuilder.bind(queueForgetPassword).to(commerceSpringDirectExchange).with(keyForgetPassword);}
    @Bean
    public Binding bindingSendMailNewPassword (Queue queueSendMailNewPassword, DirectExchange commerceSpringDirectExchange) {return BindingBuilder.bind(queueSendMailNewPassword).to(commerceSpringDirectExchange).with(keySendMailNewPassword);}
    @Bean
    public Binding bindingSaveCustomerSendEmail(Queue queueSaveCustomerSendMail, DirectExchange directExchange){return BindingBuilder.bind(queueSaveCustomerSendMail).to(directExchange).with(keySaveCustomerSendMail);}


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
