package com.OnlineShop.service.impl;

import com.OnlineShop.dto.request.order.OrderItemRequest;
import com.OnlineShop.dto.request.order.OrderRequest;
import com.OnlineShop.entity.*;
import com.OnlineShop.entity.order.Order;
import com.OnlineShop.entity.order.OrderItem;
import com.OnlineShop.enums.CountryEnum;
import com.OnlineShop.enums.RoleEnum;
import com.OnlineShop.exception.NotFoundException;
import com.OnlineShop.repository.IOrderRepository;
import com.OnlineShop.service.IOrderService;
import com.OnlineShop.service.IProductService;
import com.OnlineShop.service.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest
{

    private IOrderService underTestOrderService;


    @Mock
    private IOrderRepository orderRepository;

    @Mock
    private IUserService userService;

    @Mock
    private IProductService productService;


    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.openMocks(this);
        underTestOrderService = new OrderService(orderRepository, userService, productService);
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
    void getOrderById_shouldReturnAnOrder()
    {
        // given

        // product
        BigDecimal price = new BigDecimal("69.99");
        Category category = new Category("11", "Video Games");


        Product product = new Product(
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

        OrderItem item3 = new OrderItem(product, 19);

        List<OrderItem> orderItemList = new ArrayList<>();
        orderItemList.add(item3);

        Order order = new Order(
                "12",
                orderItemList,
                user,
                true
        );

        given(orderRepository.findById(anyString())).willReturn(Optional.of(order));

        // when
        Order foundOrder = underTestOrderService.getOrderById(anyString());

        // then
        verify(orderRepository).findById(anyString());
        assertThat(foundOrder).isEqualTo(order);
    }

    @Test
    void getOrderById_shouldThrowNotFoundException()
    {
        // given
        String orderId = "12";

        // when

        // then
        assertThatThrownBy(() -> underTestOrderService.getOrderById(orderId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Order with id: [" + orderId + "] cannot be found");

    }

    @Test
    void getUserOrders_shouldReturnUserOrderList()
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

        List<Order> userOrders = new ArrayList<>();
        userOrders.add(order1);
        userOrders.add(order2);

        given(userService.getLoggedInUser()).willReturn(user);

        given(orderRepository.findOrdersByUser(any(AppUser.class))).willReturn(userOrders);

        // when
        List<Order> foundUserOrders = underTestOrderService.getUserOrders();

        // then
        verify(orderRepository).findOrdersByUser(any(AppUser.class));
        assertThat(foundUserOrders).isEqualTo(userOrders);
    }

    @Test
    void getUserOrderById_shouldReturnUserOrder()
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

        Order userOrder = new Order(
                "11",
                orderItemList1,
                user,
                false
        );

        given(userService.getLoggedInUser()).willReturn(user);

        given(orderRepository.findOrderByUserAndId(any(AppUser.class), anyString())).willReturn(Optional.of(userOrder));

        // when
        Order foundUserOrder = underTestOrderService.getUserOrderById("11");

        // then
        verify(orderRepository).findOrderByUserAndId(any(AppUser.class), anyString());
        assertThat(foundUserOrder).isEqualTo(userOrder);
    }

    @Test
    void getUserOrderById_shouldThrowNotFoundException()
    {
        // given

        String orderId = "11";


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


        given(userService.getLoggedInUser()).willReturn(user);

        // when


        // then
        assertThatThrownBy(() -> underTestOrderService.getUserOrderById(orderId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Order with id: [" + orderId + "] and username: [" + user.getUsername() + "]  cannot be found");

    }

    @Test
    void createUserOrder_shouldReturnAnOrder()
    {
        // given

        // request
        OrderItemRequest orderItemRequest1 = new OrderItemRequest("19", 9);
//        OrderItemRequest orderItemRequest2 = new OrderItemRequest("20", 2);

        List<OrderItemRequest> orderItemRequestList = new ArrayList<>();
        orderItemRequestList.add(orderItemRequest1);
//        orderItemRequestList.add(orderItemRequest2);

        OrderRequest orderRequest = new OrderRequest(orderItemRequestList);

        // created

        BigDecimal price = new BigDecimal("69.99");
        Category category = new Category("11", "Video Games");

        Product product1 = new Product(
                "19",
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
                "20",
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
//        OrderItem item2 = new OrderItem(product1, 9);
//        OrderItem item2 = new OrderItem(product2, 2);

        List<OrderItem> orderItemList = new ArrayList<>();
        orderItemList.add(item1);
//        orderItemList.add(item2);

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

        Order order = new Order(
                "11",
                orderItemList,
                user,
                false
        );

        given(productService.subtractProductQuantity(anyString(), any())).willReturn(product1);
//        given(productService.subtractProductQuantity(anyString(), any())).willReturn(product2);

        given(userService.getLoggedInUser()).willReturn(user);
        given(orderRepository.save(any(Order.class))).willReturn(order);

        // when
        Order createdOrder = underTestOrderService.createUserOrder(orderRequest);

        // then
        ArgumentCaptor<Order> orderArgumentCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderArgumentCaptor.capture());
        Order capturedOrder = orderArgumentCaptor.getValue();

        assertThat(capturedOrder.getId()).isEqualTo(null);
        assertThat(capturedOrder.getOrderItemList().get(0).getProduct().getId()).isEqualTo(createdOrder.getOrderItemList().get(0).getProduct().getId());
        assertThat(capturedOrder.getOrderItemList().get(0).getProduct().getName()).isEqualTo(createdOrder.getOrderItemList().get(0).getProduct().getName());
        assertThat(capturedOrder.getOrderItemList().get(0).getProduct().getDescription()).isEqualTo(createdOrder.getOrderItemList().get(0).getProduct().getDescription());
        assertThat(capturedOrder.getOrderItemList().get(0).getProduct().getPrice()).isEqualTo(createdOrder.getOrderItemList().get(0).getProduct().getPrice());
        assertThat(capturedOrder.getOrderItemList().get(0).getProduct().getQuantity()).isEqualTo(createdOrder.getOrderItemList().get(0).getProduct().getQuantity());
        assertThat(capturedOrder.getOrderItemList().get(0).getProduct().getImageUrl()).isEqualTo(createdOrder.getOrderItemList().get(0).getProduct().getImageUrl());
        assertThat(capturedOrder.getOrderItemList().get(0).getProduct().getCategory()).isEqualTo(createdOrder.getOrderItemList().get(0).getProduct().getCategory());
        assertThat(capturedOrder.getOrderItemList().get(0).getProduct().isActive()).isEqualTo(createdOrder.getOrderItemList().get(0).getProduct().isActive());
        assertThat(capturedOrder.getUser()).isEqualTo(createdOrder.getUser());
        assertThat(capturedOrder.getTotalPrice()).isEqualTo(createdOrder.getTotalPrice());
        assertThat(capturedOrder.isPaymentSuccessful()).isEqualTo(createdOrder.isPaymentSuccessful());

    }
}