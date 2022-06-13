package com.OnlineShop.controller;


import com.OnlineShop.dto.request.order.OrderRequest;
import com.OnlineShop.entity.order.Order;
import com.OnlineShop.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController
{
    private final IOrderService orderService;

    @Autowired
    public OrderController(IOrderService orderService)
    {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<Order>> getOrders()
    {
        List<Order> orders = orderService.getOrders();

        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable String orderId)
    {
        Order foundOrder = orderService.getOrderById(orderId);

        return new ResponseEntity<>(foundOrder, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Order>> getUserOrders()
    {
        List<Order> orders = orderService.getUserOrders();

        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/user/{orderId}")
    public ResponseEntity<Order> getUserOrder(@PathVariable String orderId)
    {
        Order foundUserOrder = orderService.getUserOrderById(orderId);

        return new ResponseEntity<>(foundUserOrder, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Order> createUserOrder(@Valid @RequestBody OrderRequest orderRequest)
    {
        Order createdOrder = orderService.createUserOrder(orderRequest);

        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }
}
