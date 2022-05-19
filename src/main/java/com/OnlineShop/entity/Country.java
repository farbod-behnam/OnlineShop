package com.OnlineShop.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Document(collection = "Country")
public class Country
{
    @Id
    private String id;

    @NotBlank(message = "name is required")
    @Size(min = 4, max = 56, message = "name must be between 4 and 56 character")
    @Pattern(regexp = "^[a-z]+( [a-z]+)*$", message = "name should only contain lower case letter and space")
    private String name;

    public Country()
    {
    }

    public Country(String id, String name)
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
        return "Country [" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ']';
    }
}
