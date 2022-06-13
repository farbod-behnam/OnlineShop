package com.OnlineShop.service.impl;

import com.OnlineShop.dto.request.order.OrderRequest;
import com.OnlineShop.entity.order.Order;
import com.OnlineShop.repository.IOrderRepository;
import com.OnlineShop.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrderService implements IOrderService
{

    private final IOrderRepository orderRepository;

    @Autowired
    public OrderService(IOrderRepository orderRepository)
    {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> getOrders()
    {
        return orderRepository.findAll();
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
