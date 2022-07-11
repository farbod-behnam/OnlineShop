package com.OnlineShop.service;

import com.OnlineShop.dto.request.order.OrderRequest;
import com.OnlineShop.dto.request.payment.PaymentOrderRequest;
import com.OnlineShop.entity.order.Order;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IOrderService
{
    @Transactional
    List<Order> getOrders();

    @Transactional
    Order getOrderById(String orderId);

    @Transactional
    List<Order> getUserOrders();

    @Transactional
    Order getUserOrderById(String orderId);

    @Transactional
    Order getOrderByIdAndUsername(String orderId, String username);

    @Transactional
    Order createUserOrder(OrderRequest orderRequest);

    @Transactional
    Order updateUserOrderTransactionStatus(PaymentOrderRequest orderRequest);

    @Transactional
    void deleteOrderById(String orderId);
}
