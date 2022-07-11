package com.OnlineShop.service.impl;

import com.OnlineShop.dto.request.order.OrderItemRequest;
import com.OnlineShop.dto.request.order.OrderRequest;
import com.OnlineShop.entity.AppUser;
import com.OnlineShop.entity.Product;
import com.OnlineShop.entity.order.Order;
import com.OnlineShop.entity.order.OrderItem;
import com.OnlineShop.enums.TransactionStatusEnum;
import com.OnlineShop.exception.NotFoundException;
import com.OnlineShop.repository.IOrderRepository;
import com.OnlineShop.service.IOrderService;
import com.OnlineShop.rabbitmq.service.IPaymentService;
import com.OnlineShop.service.IProductService;
import com.OnlineShop.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderService implements IOrderService
{

    private final IOrderRepository orderRepository;
    private final IUserService userService;
    private final IProductService productService;
    private final IPaymentService paymentService;

    @Autowired
    public OrderService(IOrderRepository orderRepository, IUserService userService, IProductService productService, IPaymentService paymentService)
    {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.productService = productService;
        this.paymentService = paymentService;
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
        AppUser loggedInUser = userService.getLoggedInUser();

        Optional<Order> result = orderRepository.findOrderByUserAndId(loggedInUser, orderId);

        if (result.isEmpty())
            throw new NotFoundException("Order with id: [" + orderId + "] and username: [" + loggedInUser.getUsername() + "]  cannot be found");

        return result.get();
    }

    @Override
    public Order createUserOrder(OrderRequest orderRequest)
    {

        List<OrderItemRequest> orderItemRequestList = orderRequest.getOrderItemList();

        List<OrderItem> orderItemList = new ArrayList<>();


        for (OrderItemRequest orderItemRequest: orderItemRequestList)
        {
            Product product = productService.subtractProductQuantity(orderItemRequest.getProductId(), orderItemRequest.getQuantity());

            OrderItem orderItem = new OrderItem(product, orderItemRequest.getQuantity());
            orderItemList.add(orderItem);
        }

        AppUser loggedInUser = userService.getLoggedInUser();

        Order order = new Order(
                null,
                orderItemList,
                loggedInUser,
                TransactionStatusEnum.IN_PROCESS.name()
        );

        Order createdOrder = orderRepository.save(order);

        paymentService.chargeCard(createdOrder);

        return createdOrder;
    }

    @Override
    public void deleteOrderById(String orderId)
    {
        Optional<Order> result = orderRepository.findById(orderId);

        if (result.isEmpty())
            throw new NotFoundException("Order with id: [" + orderId + "] cannot be found");

        orderRepository.deleteById(orderId);
    }
}
