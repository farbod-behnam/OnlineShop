package com.OnlineShop.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

//@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "Category")
public class Category
{
    @Id
    private String id;

    @NotBlank(message = "name is required")
    @Size(min = 3, max = 16, message = "name must be between 3 and 16 characters")
    @Pattern(regexp = "[a-zA-Z ][a-zA-Z0-9 ]+", message = "name should only contain chars/digits and not start with digit")
    private String name;

    public Category()
    {
    }

    public Category(String id, String name)
    {
        this.id = id;
        this.name = name;
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

    @Override
    public String toString()
    {
        return "Category [" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ']';
    }
}
