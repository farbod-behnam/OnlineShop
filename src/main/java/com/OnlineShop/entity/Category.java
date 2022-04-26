package com.OnlineShop.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "Category")
public class Category
{
    @Id
    private String id;
    private String name;

    @DBRef
    private List<Product> products;

    public Category()
    {
    }

    public Category(String id, String name, List<Product> products)
    {
        this.id = id;
        this.name = name;
        this.products = products;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<Product> getProducts()
    {
        return products;
    }

    public void setProducts(List<Product> products)
    {
        this.products = products;
    }

    public void addProduct(Product product)
    {
        if (this.products == null)
            this.products = new ArrayList<>();

        this.products.add(product);
    }

    public void removeProduct(Product product)
    {
        if (this.products == null)
            return;

        this.products.remove(product);
    }

    @Override
    public String toString()
    {
        return "Category [" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", products=" + products +
                ']';
    }
}
