package com.OnlineShop.rabbitmq.listener;

import com.OnlineShop.dto.request.payment.PaymentOrderRequest;
import com.OnlineShop.entity.order.Order;
import com.OnlineShop.service.IOrderService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class PaymentAppListenerComponent
{
    private final IOrderService orderService;

    @Autowired
    public PaymentAppListenerComponent(IOrderService orderService)
    {
        this.orderService = orderService;
    }

    @RabbitListener(queues = "payment_app_order_queue")
    public void orderQueueListener(PaymentOrderRequest paymentOrderRequest)
    {
        orderService.updateUserOrderTransactionStatus(paymentOrderRequest);
    }

}
