package com.OnlineShop.entity.order;

import com.OnlineShop.entity.*;
import com.OnlineShop.enums.OrderStatusEnum;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class OrderTest
{

    @Test
    void calculateTotalPrice_shouldReturnTotalPrice_firstTest()
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
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );


        OrderItem item1 = new OrderItem(product1, 9);

        List<OrderItem> orderItemList = new ArrayList<>();
        orderItemList.add(item1);


        Order order = new Order(
                "11",
                orderItemList,
                null,
                OrderStatusEnum.IN_PROGRESS.name()
        );

        // when

        // then
        assertThat(order.getTotalPrice()).isEqualTo(new BigDecimal("629.91"));
    }

    @Test
    void calculateTotalPrice_shouldReturnTotalPrice_secondTest()
    {
        // given
        BigDecimal price1 = new BigDecimal("69.99");
        BigDecimal price2 = new BigDecimal("39.99");
        Category category = new Category("11", "Video Games");

        Product product1 = new Product(
                "19",
                "Bloodborne",
                "A souls like game",
                price1,
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
                price2,
                19,
                "http://image_url",
                category,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        OrderItem item1 = new OrderItem(product1, 9);
        OrderItem item2 = new OrderItem(product2, 2);

        List<OrderItem> orderItemList = new ArrayList<>();
        orderItemList.add(item1);
        orderItemList.add(item2);


        Order order = new Order(
                "11",
                orderItemList,
                null,
                OrderStatusEnum.IN_PROGRESS.name()
        );

        // when

        // then

        assertThat(order.getTotalPrice()).isEqualTo(new BigDecimal("709.89"));
    }

}