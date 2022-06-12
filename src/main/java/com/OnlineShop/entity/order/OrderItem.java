package com.OnlineShop.entity.order;

import com.OnlineShop.entity.Product;
import org.springframework.data.mongodb.core.mapping.DBRef;

public class OrderItem
{
    @DBRef
    private Product product;

    private Integer quantity;

    public OrderItem()
    {
    }

    public OrderItem(Product product, Integer quantity)
    {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct()
    {
        return product;
    }

    public void setProduct(Product product)
    {
        this.product = product;
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
        return "OrderItem [" +
                "product=" + product +
                ", quantity=" + quantity +
                ']';
    }
}
