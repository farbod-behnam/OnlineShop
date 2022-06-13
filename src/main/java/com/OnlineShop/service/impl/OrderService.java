package com.OnlineShop.service.impl;

import com.OnlineShop.dto.request.order.OrderRequest;
import com.OnlineShop.entity.order.Order;
import com.OnlineShop.exception.NotFoundException;
import com.OnlineShop.repository.IOrderRepository;
import com.OnlineShop.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
        Optional<Order> result = orderRepository.findById(orderId);

        Order order;

        if (result.isPresent())
            order = result.get();
        else
            throw new NotFoundException("Order with id: [" + orderId + "] cannot be found");

        return order;
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
