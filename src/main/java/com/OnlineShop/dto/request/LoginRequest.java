package com.OnlineShop.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class LoginRequest
{
    @NotBlank(message = "username is required")
    @Pattern(regexp = "^[a-z]*?[0-9]*$", message = "username is invalid")
    private String username;

    @NotBlank(message = "password is required")
    private String password;

    public LoginRequest()
    {
    }

    public LoginRequest(String username, String password)
    {
        this.username = username;
        this.password = password;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    @Override
    public String toString()
    {
        return "LoginRequest [" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ']';
    }
}
