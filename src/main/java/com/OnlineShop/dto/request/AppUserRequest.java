package com.OnlineShop.dto.request;

import com.OnlineShop.validation.PhoneNumber;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.*;
import java.util.List;

public class AppUserRequest
{
    private String id;

    @NotBlank(message = "first name is required")
    @Size(min = 3, max = 25, message = "first name must be between 3 and 25 character")
    @Pattern(regexp = "^[a-zA-Z]*$", message = "first name should only contain alphabet character")
    private String firstName;

    @NotBlank(message = "last name is required")
    @Size(min = 3, max = 25, message = "last name must be between 3 and 25 character")
    @Pattern(regexp = "^[a-zA-Z]*$", message = "last name should only contain alphabet character")
    private String lastName;


    @NotBlank(message = "phone number is required")
    @Pattern(regexp = "^[0-9]*$", message = "phone number must only contain number")
    @PhoneNumber(countryCode = {"001", "0049", "0044"}, phoneNumberLength = {13, 14, 14}, message = "phone number is invalid")
    private String phoneNumber;

    @NotBlank(message = "email is required")
    @Email(message = "email is invalid")
    private String email;

    @NotNull(message = "role is required")
    @Size(min = 1, max = 2, message = "user must have at least one role and at max two roles")
    private List<String> roleIdList;

    @NotBlank(message = "username is required")
    @Size(min = 3, max = 24, message = "username must be between 3 and 24 character")
    @Pattern(regexp = "^[a-z]*?[0-9]*$", message = "username is invalid")
    private String username;

    @JsonIgnore
    @NotBlank(message = "password is required")
    @Size(min = 10, max = 64, message = "password must be greater than 10 character")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[*.!@$%^& ]).+$", message = "password must contain at least one upper case letter, one lower case letter, number and special character")
    private String password;

    @NotNull(message = "country is required")
    private String  countryId;

    @NotNull(message = "address is required")
    @Size(max = 100, message = "address must be less than 100 character")
    @Pattern(regexp = "[A-Za-z0-9'.\\-\\s,()]*", message = "address is invalid")
    private String address;



    public AppUserRequest()
    {
    }

    public AppUserRequest(String id, String firstName, String lastName, String phoneNumber, String email, List<String> roleIdList, String username, String password, String countryId, String address)
    {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.roleIdList = roleIdList;
        this.username = username;
        this.password = password;
        this.countryId = countryId;
        this.address = address;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public List<String> getRolesId()
    {
        return roleIdList;
    }

    public void setRolesId(List<String> roleIdList)
    {
        this.roleIdList = roleIdList;
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

    public String getCountryId()
    {
        return countryId;
    }

    public void setCountryId(String countryId)
    {
        this.countryId = countryId;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }


    @Override
    public String toString()
    {
        return "AppUserDto [" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", rolesId=" + roleIdList +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", countryId='" + countryId + '\'' +
                ", address='" + address + '\'' +
                ']';
    }
}
