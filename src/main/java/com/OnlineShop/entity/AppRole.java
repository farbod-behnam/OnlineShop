package com.OnlineShop.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Document(collection = "Role")
public class AppRole
{
    @Id
    private String id;

    @NotBlank(message = "name is required")
    @Size(min = 7, max = 16, message = "name must be between 7 and 16 character")
    @Pattern(regexp = "^[A-Z]+[_][A-Z]+$", message = "only upper case with one underscore between ex: ROLE_USER")
    private String name;

    public AppRole()
    {
    }

    public AppRole(String id, String name)
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
        return "AppRole [" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ']';
    }
}
