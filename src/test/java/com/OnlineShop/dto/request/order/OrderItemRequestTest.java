package com.OnlineShop.dto.request.order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;


import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class OrderItemRequestTest
{

    private Validator validator;

    @BeforeEach
    void setUp()
    {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void orderItemRequest_validOrderItemRequest()
    {
        // given

        // request
        OrderItemRequest orderItemRequest = new OrderItemRequest("19", 9);


        // when

        // then
        Set<ConstraintViolation<OrderItemRequest>> violations = validator.validate(orderItemRequest);

        assertThat(violations.isEmpty()).isTrue();
    }

    @Test
    public void orderItemRequest_nullProductId_shouldNotValidate()
    {
        // given

        // request
        OrderItemRequest orderItemRequest = new OrderItemRequest(null, 9);


        // when

        // then
        Set<ConstraintViolation<OrderItemRequest>> violations = validator.validate(orderItemRequest);

        for (ConstraintViolation<OrderItemRequest> violation : violations)
        {
            System.out.println("Violation Message: " + violation.getMessage());
        }

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<OrderItemRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("product id is required");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("productId");
        assertThat(violation.getInvalidValue()).isEqualTo(null);
    }

    @Test
    public void orderItemRequest_nullQuantity_shouldNotValidate()
    {
        // given

        // request
        OrderItemRequest orderItemRequest = new OrderItemRequest("19", null);


        // when

        // then
        Set<ConstraintViolation<OrderItemRequest>> violations = validator.validate(orderItemRequest);

        for (ConstraintViolation<OrderItemRequest> violation : violations)
        {
            System.out.println("Violation Message: " + violation.getMessage());
        }

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<OrderItemRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("quantity is required");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("quantity");
        assertThat(violation.getInvalidValue()).isEqualTo(null);
    }

    @Test
    public void orderItemRequest_quantityLessThanOne_shouldNotValidate()
    {
        // given

        // request
        OrderItemRequest orderItemRequest = new OrderItemRequest("19", 0);


        // when

        // then
        Set<ConstraintViolation<OrderItemRequest>> violations = validator.validate(orderItemRequest);

        for (ConstraintViolation<OrderItemRequest> violation : violations)
        {
            System.out.println("Violation Message: " + violation.getMessage());
        }

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<OrderItemRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("quantity must be greater than or equal to 1");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("quantity");
        assertThat(violation.getInvalidValue()).isEqualTo(0);
    }

    @Test
    public void orderItemRequest_quantityGreaterThan1001_shouldNotValidate()
    {
        // given

        // request
        OrderItemRequest orderItemRequest = new OrderItemRequest("19", 1001);


        // when

        // then
        Set<ConstraintViolation<OrderItemRequest>> violations = validator.validate(orderItemRequest);

        for (ConstraintViolation<OrderItemRequest> violation : violations)
        {
            System.out.println("Violation Message: " + violation.getMessage());
        }

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<OrderItemRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("quantity must be less than or equal to 1000");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("quantity");
        assertThat(violation.getInvalidValue()).isEqualTo(1001);
    }

}