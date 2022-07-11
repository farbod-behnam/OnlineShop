package com.OnlineShop.rabbitmq.service;

import com.OnlineShop.entity.AppUser;
import com.OnlineShop.entity.order.Order;
import org.springframework.stereotype.Service;

@Service
public interface IPaymentService
{
    /**
     * It sends the paymentOrderRequest to payment application and
     * charges the wallet card of specified user based on the user id
     * @param order {@link Order} argument for sending information to payment application
     */
    void chargeCard(Order order);

    /**
     * it sends the user to payment application for creating or updating
     * the user table in payment application
     * @param user {@link AppUser} argument for sending user information to payment application
     */
    void saveUserRecord(AppUser user);
}
