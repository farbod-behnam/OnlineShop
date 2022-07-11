package com.OnlineShop.rabbitmq.service.impl;

import com.OnlineShop.dto.request.payment.PaymentOrderRequest;
import com.OnlineShop.dto.request.payment.PaymentUserRequest;
import com.OnlineShop.entity.AppUser;
import com.OnlineShop.entity.order.Order;
import com.OnlineShop.exception.RabbitMQException;
import com.OnlineShop.repository.IOrderRepository;
import com.OnlineShop.repository.IUserRepository;
import com.OnlineShop.rabbitmq.service.IPaymentService;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PaymentService implements IPaymentService
{


    @Value("${onlineshop.app.rabbitmq.exchange}")
    private String EXCHANGE;

    @Value("${onlineshop.app.rabbitmq.routingKey.order}")
    private String ORDER_ROUTING_KEY;

    @Value("${onlineshop.app.rabbitmq.routingKey.user}")
    private String USER_ROUTING_KEY;

    private final RabbitTemplate rabbitTemplate;
    private final IOrderRepository orderRepository;
    private final IUserRepository userRepository;

    @Autowired
    public PaymentService(RabbitTemplate template, IOrderRepository orderRepository, IUserRepository userRepository)
    {
        this.rabbitTemplate = template;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void chargeCard(Order order)
    {

        PaymentOrderRequest paymentOrderRequest = new PaymentOrderRequest(
                order.getId(),
                order.getUser().getUsername(),
                order.getTotalPrice(),
                order.getTransactionStatus()
        );

        try
        {
            rabbitTemplate.convertAndSend(EXCHANGE, ORDER_ROUTING_KEY, paymentOrderRequest);
        }
        catch (AmqpException e)
        {
            e.printStackTrace();
            orderRepository.deleteById(order.getId());
            throw new RabbitMQException("Transaction cannot be completed");
        }
    }

    @Override
    public void saveUserRecord(AppUser user)
    {

        PaymentUserRequest paymentUserRequest = new PaymentUserRequest(
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.getEmail(),
                user.getUsername(),
                user.getPassword(),
                user.getCountry().getName(),
                user.getAddress(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );

        try
        {
            rabbitTemplate.convertAndSend(EXCHANGE, USER_ROUTING_KEY, paymentUserRequest);
        }
        catch (AmqpException e)
        {
            e.printStackTrace();
            userRepository.deleteById(user.getId());
            throw new RabbitMQException("User record cannot be saved");
        }
    }
}
