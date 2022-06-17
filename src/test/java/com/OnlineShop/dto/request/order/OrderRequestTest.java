package com.OnlineShop.dto.request.order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


class OrderRequestTest
{
    private Validator validator;


    @BeforeEach
    void setUp()
    {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void orderRequest_validOrderRequest()
    {
        // given

        // request
        OrderItemRequest orderItemRequest1 = new OrderItemRequest("19", 9);
        OrderItemRequest orderItemRequest2 = new OrderItemRequest("20", 2);

        List<OrderItemRequest> orderItemRequestList = new ArrayList<>();
        orderItemRequestList.add(orderItemRequest1);
        orderItemRequestList.add(orderItemRequest2);

        OrderRequest orderRequest = new OrderRequest(orderItemRequestList);

        // when

        // then
        Set<ConstraintViolation<OrderRequest>> violations = validator.validate(orderRequest);

        assertThat(violations.isEmpty()).isTrue();
    }

    @Test
    public void orderRequest_nullRequestItem_shouldNotValidate()
    {
        // given

        // request

        OrderRequest orderRequest = new OrderRequest(null);

        // when

        // then
        Set<ConstraintViolation<OrderRequest>> violations = validator.validate(orderRequest);

        for (ConstraintViolation<OrderRequest> violation : violations)
        {
            System.out.println("Violation Message: " + violation.getMessage());
        }

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<OrderRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("order item is required");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("orderItemList");
        assertThat(violation.getInvalidValue()).isEqualTo(null);
    }

    @Test
    public void orderRequest_emptyRequestItem_shouldNotValidate()
    {
        // given

        // request

        List<OrderItemRequest> orderItemRequestList = new ArrayList<>();

        OrderRequest orderRequest = new OrderRequest(orderItemRequestList);

        // when

        // then
        Set<ConstraintViolation<OrderRequest>> violations = validator.validate(orderRequest);

        for (ConstraintViolation<OrderRequest> violation : violations)
        {
            System.out.println("Violation Message: " + violation.getMessage());
        }

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(2);
    }
}