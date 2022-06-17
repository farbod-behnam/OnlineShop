package com.OnlineShop.dto.request.order;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class OrderItemRequest
{
    @NotBlank(message = "product id is required")
    private String productId;

    @NotNull(message = "quantity is required")
    @Min(value = 1, message = "quantity must be greater than or equal to 1")
    @Max(value = 1000, message = "quantity must be less than or equal to 1000")
    private Integer quantity;

    public OrderItemRequest()
    {
    }

    public OrderItemRequest(String productId, Integer quantity)
    {
        this.productId = productId;
        this.quantity = quantity;
    }

    public String getProductId()
    {
        return productId;
    }

    public void setProductId(String productId)
    {
        this.productId = productId;
    }

    public Integer getQuantity()
    {
        return quantity;
    }

    public void setQuantity(Integer quantity)
    {
        this.quantity = quantity;
    }

    @Override
    public String toString()
    {
        return "OrderItemRequest [" +
                "productId='" + productId + '\'' +
                ", quantity=" + quantity +
                ']';
    }
}
