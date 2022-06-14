package com.OnlineShop.service.impl;

import com.OnlineShop.dto.request.order.OrderRequest;
import com.OnlineShop.entity.AppUser;
import com.OnlineShop.entity.order.Order;
import com.OnlineShop.exception.NotFoundException;
import com.OnlineShop.repository.IOrderRepository;
import com.OnlineShop.service.IOrderService;
import com.OnlineShop.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderService implements IOrderService
{

    private final IOrderRepository orderRepository;
    private final IUserService userService;

    @Autowired
    public OrderService(IOrderRepository orderRepository, IUserService userService)
    {
        this.orderRepository = orderRepository;
        this.userService = userService;
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
        AppUser loggedInUser = userService.getLoggedInUser();

        return orderRepository.findOrdersByUser(loggedInUser);
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
