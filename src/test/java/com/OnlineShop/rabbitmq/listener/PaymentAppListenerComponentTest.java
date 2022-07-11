package com.OnlineShop.rabbitmq.listener;

import com.OnlineShop.dto.request.payment.PaymentOrderRequest;
import com.OnlineShop.entity.*;
import com.OnlineShop.entity.order.Order;
import com.OnlineShop.entity.order.OrderItem;
import com.OnlineShop.enums.CountryEnum;
import com.OnlineShop.enums.RoleEnum;
import com.OnlineShop.enums.TransactionStatusEnum;
import com.OnlineShop.service.IOrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PaymentAppListenerComponentTest
{
    private PaymentAppListenerComponent underTestPaymentAppListenerComponent;

    @Mock
    private IOrderService orderService;

    @BeforeEach
    void setUp()
    {
        underTestPaymentAppListenerComponent = new PaymentAppListenerComponent(orderService);
    }

    @Test
    void orderQueueListener_shouldGetPaymentOrderRequest()
    {
        // given


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
                TransactionStatusEnum.IN_PROCESS.name()
        );

        PaymentOrderRequest paymentOrderRequest = new PaymentOrderRequest(
                userOrder.getId(),
                user.getUsername(),
                userOrder.getTotalPrice(),
                TransactionStatusEnum.NOT_ENOUGH_CREDITS.name()
        );



        // when
        underTestPaymentAppListenerComponent.orderQueueListener(paymentOrderRequest);

        // then
        ArgumentCaptor<PaymentOrderRequest> paymentOrderRequestArgumentCaptor = ArgumentCaptor.forClass(PaymentOrderRequest.class);
        verify(orderService).updateUserOrderTransactionStatus(paymentOrderRequestArgumentCaptor.capture());
        PaymentOrderRequest capturedPaymentOrderRequest = paymentOrderRequestArgumentCaptor.getValue();

        assertThat(capturedPaymentOrderRequest).isEqualTo(paymentOrderRequest);
    }
}