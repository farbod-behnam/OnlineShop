package com.OnlineShop.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig
{

    @Value("${onlineshop.app.rabbitmq.exchange}")
    private String EXCHANGE;

    @Value("${onlineshop.app.rabbitmq.queue.order}")
    private String ORDER_QUEUE;

    @Value("${onlineshop.app.rabbitmq.routingKey.order}")
    private String ORDER_ROUTING_KEY;

    @Value("${onlineshop.app.rabbitmq.queue.user}")
    private String USER_QUEUE;

    @Value("${onlineshop.app.rabbitmq.routingKey.user}")
    private String USER_ROUTING_KEY;

    @Bean
    public TopicExchange exchange()
    {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue orderQueue()
    {
        return new Queue(ORDER_QUEUE);
    }

    @Bean
    public Queue userQueue()
    {
        return new Queue(USER_QUEUE);
    }

    @Bean
    public Binding orderBinding(Queue orderQueue, TopicExchange exchange)
    {
        return BindingBuilder.bind(orderQueue).to(exchange).with(ORDER_ROUTING_KEY);
    }

    @Bean
    public Binding userBinding(Queue userQueue, TopicExchange exchange)
    {
        return BindingBuilder.bind(userQueue).to(exchange).with(USER_ROUTING_KEY);
    }

    /**
     * since we use object to send message to rabbitMQ, therefore
     * we need to convert it
     */
    @Bean
    public MessageConverter converter()
    {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * With this rabbit template we can publish and consume the message
     */
    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory)
    {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }

}
