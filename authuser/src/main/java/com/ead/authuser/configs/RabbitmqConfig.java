package com.ead.authuser.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {



    // Way of Connect that Application with the Instance of RabbitMQ
    @Autowired
    private CachingConnectionFactory cachingConnectionFactory;

    // Exchange that user will publish the events
    @Value(value = "${ead.broker.exchange.userEvent}")
    private String exchangeUserEvent;


    // Objective = Build a bean of RabbitMQ to make Independence Injection on our Publish ( Public Events)
    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate((cachingConnectionFactory));   // Making the connection.
        template.setMessageConverter(messageConverter()); //
        return template;
    }


    // -> Always when we send and receive messages to the Broker we need to use this  conversion... principally when we are using Date...
    // -> Serializing and Deserializing
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    //Defining the FanoutExchange, Product Method
    // If send  a message without consumer...the message will be lost
    @Bean
    public FanoutExchange fanoutUserEvent(){
       return new FanoutExchange(exchangeUserEvent); // Name of the exchange
    }

}
