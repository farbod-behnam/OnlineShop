package com.OnlineShop.dto.request;


import javax.validation.constraints.*;
import java.math.BigDecimal;

public class ProductRequest
{
    private String id;

    @NotBlank(message = "name is required")
    @Size(min = 3, max = 45, message = "name must be between 3 and 45 character")
    @Pattern(regexp = "[a-zA-Z][a-zA-Z0-9 ]+", message = "name should only contain chars/digits")
    private String name;

    @NotBlank(message = "description is required")
    @Size(min = 15, max = 128, message = "description must be between 15 and 128 character")
    @Pattern(regexp = "[a-zA-Z0-9 ]+", message = "description should only contain chars/digits")
    private String description;

    @NotNull(message = "price is required")
    @DecimalMin(value = "0.00", inclusive = false, message = "price must be greater than 0.0")
    @Digits(integer = 5, fraction = 2, message = "price must be between 0.00 and 99999.99")
    private BigDecimal price;

    @NotNull(message = "quantity is required")
    @Min(value = 0, message = "quantity must be greater than or equal to 0")
    @Max(value = 1000, message = "quantity must be less than or equal to 1000")
    private Integer quantity;

    private String imageUrl;

    @NotBlank(message = "category is required")
    String categoryId;


    @NotNull(message = "active is required")
    private Boolean active;


    public ProductRequest()
    {
    }

    public ProductRequest(String id, String name, String description, BigDecimal price, Integer quantity, String imageUrl, String categoryId, Boolean active)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
        this.active = active;
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

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public BigDecimal getPrice()
    {
        return price;
    }

    public void setPrice(BigDecimal price)
    {
        this.price = price;
    }

    public Integer getQuantity()
    {
        return quantity;
    }

    public void setQuantity(Integer quantity)
    {
        this.quantity = quantity;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
    }

    public String getCategoryId()
    {
        return categoryId;
    }

    public void setCategoryId(String categoryId)
    {
        this.categoryId = categoryId;
    }

    public Boolean getActive()
    {
        return active;
    }

    public void setActive(Boolean active)
    {
        this.active = active;
    }



    @Override
    public String toString()
    {
        return "ProductDto [" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", imageUrl='" + imageUrl + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", active=" + active +
                ']';
    }
}
