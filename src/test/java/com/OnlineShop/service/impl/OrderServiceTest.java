package com.OnlineShop.service.impl;

import com.OnlineShop.entity.*;
import com.OnlineShop.entity.order.Order;
import com.OnlineShop.entity.order.OrderItem;
import com.OnlineShop.enums.CountryEnum;
import com.OnlineShop.enums.RoleEnum;
import com.OnlineShop.repository.IOrderRepository;
import com.OnlineShop.service.IOrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest
{

    private IOrderService underTestOrderService;

    @Mock
    private IOrderRepository orderRepository;

    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.openMocks(this);
        underTestOrderService = new OrderService(orderRepository);
    }

    @Test
    void getOrders_shouldReturnOrderList()
    {
        // given

        // order
        BigDecimal price = new BigDecimal("69.99");
        Category category = new Category("11", "Video Games");

        Product product1 = new Product(
                "18",
                "Bloodborne",
                "A souls like game",
                price,
                19,
                "http://image_url",
                category,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Product product2 = new Product(
                "19",
                "The Last of Us",
                "A narrative game with action sequences",
                price,
                19,
                "http://image_url",
                category,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        OrderItem item1 = new OrderItem(product1, 9);
        OrderItem item2 = new OrderItem(product2, 2);

        List<OrderItem> orderItemList1 = new ArrayList<>();
        orderItemList1.add(item1);
        orderItemList1.add(item2);


        // user
        Set<AppRole> roles = new HashSet<>();

        Country country = new Country("10", CountryEnum.Germany.name());
        AppRole role = new AppRole("11", RoleEnum.ROLE_USER.name());

        roles.add(role);

        AppUser user = new AppUser(
                "19",
                "John",
                "Wick",
                "001666666666",
                "john.wick@gmail.com",
                roles,
                "john.wick",
                "Password!1234",
                country,
                "This is an address",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Order order1 = new Order(
                "11",
                orderItemList1,
                user,
                false
        );


        OrderItem item3 = new OrderItem(product2, 19);

        List<OrderItem> orderItemList2 = new ArrayList<>();
        orderItemList2.add(item3);

        Order order2 = new Order(
                "12",
                orderItemList2,
                user,
                true
        );

        List<Order> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);

        given(orderRepository.findAll()).willReturn(orders);

        // when
        underTestOrderService.getOrders();

        // then
        verify(orderRepository).findAll();
    }

    @Test
    void getOrderById()
    {
    }

    @Test
    void getUserOrders()
    {
    }

    @Test
    void getUserOrderById()
    {
    }

    @Test
    void createUserOrder()
    {
    }
}