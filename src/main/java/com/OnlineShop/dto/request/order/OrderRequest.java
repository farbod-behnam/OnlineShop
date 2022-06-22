package com.OnlineShop.dto.request.order;

import com.OnlineShop.entity.order.OrderItem;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

public class OrderRequest
{
    @NotEmpty(message = "order item is required")
    @Size(min = 1, message = "order item must be greater than 1")
    @Valid
    private List<OrderItemRequest> orderItemList;

    public OrderRequest()
    {
    }


    public OrderRequest(List<OrderItemRequest> orderItemList)
    {
        this.orderItemList = orderItemList;
    }

    public List<OrderItemRequest> getOrderItemList()
    {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItemRequest> orderItemList)
    {
        this.orderItemList = orderItemList;
    }

    @Override
    public String toString()
    {
        return "OrderRequest [" +
                "orderItemList=" + orderItemList +
                ']';
    }
}
