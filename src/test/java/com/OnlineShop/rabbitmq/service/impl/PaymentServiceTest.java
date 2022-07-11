package com.OnlineShop.rabbitmq.service.impl;

import com.OnlineShop.dto.request.payment.PaymentOrderRequest;
import com.OnlineShop.dto.request.payment.PaymentUserRequest;
import com.OnlineShop.entity.*;
import com.OnlineShop.entity.order.Order;
import com.OnlineShop.entity.order.OrderItem;
import com.OnlineShop.enums.CountryEnum;
import com.OnlineShop.enums.RoleEnum;
import com.OnlineShop.enums.TransactionStatusEnum;
import com.OnlineShop.repository.IOrderRepository;
import com.OnlineShop.repository.IUserRepository;
import com.OnlineShop.rabbitmq.service.IPaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

//@ExtendWith(MockitoExtension.class)
class PaymentServiceTest
{
    private IPaymentService underTestPaymentService;

    private RabbitTemplate rabbitTemplate;

    @Value("${onlineshop.app.rabbitmq.exchange}")
    private String EXCHANGE;

    @Value("${onlineshop.app.rabbitmq.routingKey.order}")
    private String ORDER_ROUTING_KEY;

    @Value("${onlineshop.app.rabbitmq.routingKey.user}")
    private String USER_ROUTING_KEY;

    @Mock
    private IOrderRepository orderRepository;

    @Mock
    private IUserRepository userRepository;

    @BeforeEach
    void setUp()
    {
//        MockitoAnnotations.openMocks(this);
        rabbitTemplate = Mockito.mock(RabbitTemplate.class);
        underTestPaymentService = new PaymentService(rabbitTemplate, orderRepository, userRepository);
    }

    @Test
    public void chargeCard_orderQueue_messageShouldBePublished()
    {
        // given

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
                LocalDateTime.now(),
                LocalDateTime.now()
        );


        OrderItem item1 = new OrderItem(product1, 9);

        List<OrderItem> orderItemList = new ArrayList<>();
        orderItemList.add(item1);

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
                TransactionStatusEnum.IN_PROCESS.name()
        );

        PaymentOrderRequest paymentOrderRequest = new PaymentOrderRequest(
                order.getId(),
                order.getUser().getUsername(),
                order.getTotalPrice(),
                order.getTransactionStatus()
        );

        // then
        assertThatCode(() -> this.underTestPaymentService.chargeCard(order)).doesNotThrowAnyException();


        ArgumentCaptor<PaymentOrderRequest> paymentOrderRequestArgumentCaptor = ArgumentCaptor.forClass(PaymentOrderRequest.class);
        verify(rabbitTemplate).convertAndSend(eq(EXCHANGE), eq(ORDER_ROUTING_KEY), paymentOrderRequestArgumentCaptor.capture());
        PaymentOrderRequest capturedPaymentOrderRequest = paymentOrderRequestArgumentCaptor.getValue();

        assertThat(capturedPaymentOrderRequest.getOrderId()).isEqualTo(paymentOrderRequest.getOrderId());
        assertThat(capturedPaymentOrderRequest.getUsername()).isEqualTo(paymentOrderRequest.getUsername());
        assertThat(capturedPaymentOrderRequest.getTotalPrice()).isEqualTo(paymentOrderRequest.getTotalPrice());
        assertThat(capturedPaymentOrderRequest.getTransactionStatus()).isEqualTo(paymentOrderRequest.getTransactionStatus());
    }


    @Test
    void saveUserRecord_userQueue_messageShouldBePublished()
    {
        // given
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
                "password1234",
                country,
                "This is an address",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        PaymentUserRequest paymentUserRequest = new PaymentUserRequest(
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.getEmail(),
                user.getUsername(),
                user.getPassword(),
                user.getCountry().getName(),
                user.getAddress(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );

        // then

        assertThatCode(() -> this.underTestPaymentService.saveUserRecord(user)).doesNotThrowAnyException();

        ArgumentCaptor<PaymentUserRequest> paymentUserRequestArgumentCaptor = ArgumentCaptor.forClass(PaymentUserRequest.class);
        verify(rabbitTemplate).convertAndSend(eq(EXCHANGE), eq(USER_ROUTING_KEY), paymentUserRequestArgumentCaptor.capture());
        PaymentUserRequest capturedPaymentUserRequest = paymentUserRequestArgumentCaptor.getValue();

        assertThat(capturedPaymentUserRequest.getFirstName()).isEqualTo(paymentUserRequest.getFirstName());
        assertThat(capturedPaymentUserRequest.getLastName()).isEqualTo(paymentUserRequest.getLastName());
        assertThat(capturedPaymentUserRequest.getPhoneNumber()).isEqualTo(paymentUserRequest.getPhoneNumber());
        assertThat(capturedPaymentUserRequest.getEmail()).isEqualTo(paymentUserRequest.getEmail());
        assertThat(capturedPaymentUserRequest.getUsername()).isEqualTo(paymentUserRequest.getUsername());
        assertThat(capturedPaymentUserRequest.getPassword()).isEqualTo(paymentUserRequest.getPassword());
        assertThat(capturedPaymentUserRequest.getCountry()).isEqualTo(paymentUserRequest.getCountry());
        assertThat(capturedPaymentUserRequest.getAddress()).isEqualTo(paymentUserRequest.getAddress());
        assertThat(capturedPaymentUserRequest.getCreatedAt()).isEqualTo(paymentUserRequest.getCreatedAt());
        assertThat(capturedPaymentUserRequest.getUpdatedAt()).isEqualTo(paymentUserRequest.getUpdatedAt());
    }


}