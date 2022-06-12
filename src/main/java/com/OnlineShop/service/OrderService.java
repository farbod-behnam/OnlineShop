package com.OnlineShop.service;

import com.OnlineShop.dto.request.order.OrderRequest;
import com.OnlineShop.entity.order.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrderService implements IOrderService
{

    @Override
    public List<Order> getOrders()
    {
        return null;
    }

    @Override
    public Order getOrderById(String orderId)
    {
        return null;
    }

    @Override
    public List<Order> getUserOrders()
    {
        return null;
    }

    @Override
    public Order getUserOrderById(String orderId)
    {
        return null;
    }

    @Override
    public Order createUserOrder(OrderRequest orderRequest)
    {
        return null;
    }
}
