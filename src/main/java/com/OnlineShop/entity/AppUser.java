package com.OnlineShop.entity;

import com.OnlineShop.validation.PhoneNumber;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Document(collection = "User")
public class AppUser
{
    @Id
    private String id;

    @NotBlank(message = "first name is required")
    @Size(min = 3, max = 25, message = "first name must be between 3 and 25 character")
    @Pattern(regexp = "^[a-zA-Z]*$", message = "first name should only contain alphabet character")
    private String firstName;

    @NotBlank(message = "last name is required")
    @Size(min = 3, max = 25, message = "last name must be between 3 and 25 character")
    @Pattern(regexp = "^[a-zA-Z]*$", message = "last name should only contain alphabet character")
    private String lastName;


    @Pattern(regexp = "^[0-9]*$", message = "phone number must only contain number")
    @PhoneNumber(countryCode = {"001", "0049", "0044"}, phoneNumberLength = {13, 14, 14}, message = "phone number is invalid")
    private String phoneNumber;

    @NotBlank(message = "email is required")
    @Email(message = "email is invalid")
    private String email;

    @DBRef
    @NotNull(message = "role is required")
    private Set<AppRole> roles;

    @NotBlank(message = "username is required")
    @Pattern(regexp = "^[a-z]*\\.?[a-z]*$", message = "username is invalid")
    private String username;

    @JsonIgnore
    @Size(min = 10, max = 64, message = "password must be greater than 10 character")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[*.!@$%^& ]).{10,}$", message = "password is invalid")
    private String password;

    @DBRef
    @NotNull(message = "country is required")
    private Country country;

    @NotNull(message = "address is required")
    @Size(min = 24, max = 256, message = "address must be between 24 and 256 character")
    @Pattern(regexp = "[A-Za-z0-9'.\\-\\s,()]*", message = "address is invalid")
    private String address;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public AppUser()
    {
    }

    public AppUser(String id, String firstName, String lastName, String phoneNumber, String email, Set<AppRole> roles, String username, String password, Country country, String address, LocalDateTime createdAt, LocalDateTime updatedAt)
    {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.roles = roles;
        this.username = username;
        this.password = password;
        this.country = country;
        this.address = address;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public Set<AppRole> getRoles()
    {
        return roles;
    }

    public void setRoles(Set<AppRole> roles)
    {
        this.roles = roles;
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

    public Country getCountry()
    {
        return country;
    }

    public void setCountry(Country country)
    {
        this.country = country;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public LocalDateTime getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt()
    {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt)
    {
        this.updatedAt = updatedAt;
    }

    public void addRole(AppRole role)
    {
        if (roles == null)
            roles = new HashSet<>();

        roles.add(role);
    }

    public void removeRole(AppRole role)
    {
        if (roles == null)
            roles = new HashSet<>();

        roles.remove(role);
    }

    @Override
    public String toString()
    {
        return "User [" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", country=" + country +
                ", address='" + address + '\'' +
                ']';
    }
}
