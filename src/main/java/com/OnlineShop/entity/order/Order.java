package com.OnlineShop.entity.order;

import com.OnlineShop.entity.AppUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "Order")
public class Order
{
    @Id
    private String id;

    private List<OrderItem> orderItemList;

    @JsonIgnore
    @DBRef
    private AppUser user;

    private BigDecimal totalPrice;

    private LocalDateTime createdAt;

    private String transactionStatus;

    public Order()
    {
    }

    public Order(String id, List<OrderItem> orderItemList, AppUser user, String transactionStatus)
    {
        this.id = id;
        this.orderItemList = orderItemList;
        this.user = user;
        this.totalPrice = calculateTotalPrice();
        this.createdAt = LocalDateTime.now();
        this.transactionStatus = transactionStatus;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public List<OrderItem> getOrderItemList()
    {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList)
    {
        this.orderItemList = orderItemList;
    }

    public AppUser getUser()
    {
        return user;
    }

    public void setUser(AppUser user)
    {
        this.user = user;
    }

    public BigDecimal getTotalPrice()
    {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice)
    {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
    }

    public String getTransactionStatus()
    {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus)
    {
        this.transactionStatus = transactionStatus;
    }

    /**
     * returns total price of order. It iterates the order item list
     * ({@link OrderItem}) and multiplies each product with its quantity, then returns
     * the total price of all products
     *
     * @return totalPrice: total price of all products for this order
     */
    private BigDecimal calculateTotalPrice()
    {
        BigDecimal totalPrice = new BigDecimal("0.0");

        for (OrderItem orderItem: orderItemList)
        {
            BigDecimal productPrice = orderItem.getProduct().getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()));
            totalPrice = totalPrice.add(productPrice);
        }

        return totalPrice;
    }

    @Override
    public String toString()
    {
        return "Order [" +
                "id='" + id + '\'' +
                ", orderItemList=" + orderItemList +
                ", user=" + user +
                ", totalPrice=" + totalPrice +
                ", createdAt=" + createdAt +
                ", transactionStatus=" + transactionStatus +
                ']';
    }
}
