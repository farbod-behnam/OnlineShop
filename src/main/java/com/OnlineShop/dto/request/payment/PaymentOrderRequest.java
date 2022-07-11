package com.OnlineShop.dto.request.payment;

import java.math.BigDecimal;

/**
 * It's a DTO that sends the order data from OnlineShop to PaymentApplication
 * in order to make a transaction for the specified user who ordered some products
 */
public class PaymentOrderRequest
{
    private String orderId;
    private String username;
    private BigDecimal totalPrice;
    private String transactionStatus;

    public PaymentOrderRequest()
    {
    }

    public PaymentOrderRequest(String orderId, String username, BigDecimal totalPrice, String transactionStatus)
    {
        this.orderId = orderId;
        this.username = username;
        this.totalPrice = totalPrice;
        this.transactionStatus = transactionStatus;
    }

    public String getOrderId()
    {
        return orderId;
    }

    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public BigDecimal getTotalPrice()
    {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice)
    {
        this.totalPrice = totalPrice;
    }

    public String getTransactionStatus()
    {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus)
    {
        this.transactionStatus = transactionStatus;
    }

    @Override
    public String toString()
    {
        return "PaymentOrderRequest [" +
                "orderId='" + orderId + '\'' +
                ", username='" + username + '\'' +
                ", totalPrice=" + totalPrice +
                ", transactionStatus='" + transactionStatus + '\'' +
                ']';
    }
}
