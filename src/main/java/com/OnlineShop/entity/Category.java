package com.OnlineShop.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

//@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "Category")
public class Category
{
    @Id
    private String id;

    @NotBlank(message = "Category name is required")
    @Pattern(regexp = "[a-zA-Z][a-zA-Z0-9 ]+", message = "name should only contain chars/digits")
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
