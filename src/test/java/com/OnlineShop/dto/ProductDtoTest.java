package com.OnlineShop.dto;

import com.OnlineShop.dto.request.ProductRequest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ProductDtoTest
{
    
    private Validator validator;
    
    
    @BeforeEach
    void setUp()
    {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();


        // -----------------------------------------------------
        // for comparison of BigDecimal this config is necessary
        // -----------------------------------------------------

        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);

        Configuration.setDefaults(new Configuration.Defaults() {

            private final JsonProvider jsonProvider = new JacksonJsonProvider(objectMapper);
            private final MappingProvider mappingProvider = new JacksonMappingProvider(objectMapper);

            @Override
            public JsonProvider jsonProvider() {
                return jsonProvider;
            }

            @Override
            public MappingProvider mappingProvider() {
                return mappingProvider;
            }

            @Override
            public Set<Option> options() {
                return EnumSet.noneOf(Option.class);
            }
        });
        // -----------------------------------------------------
        // end of configuration
        // -----------------------------------------------------
    }


    @Test
    public void productDto_validProductDto()
    {
        // given
        BigDecimal price = new BigDecimal("69.99");

        ProductRequest productDto = new ProductRequest(
                "19",
                "Bloodborne",
                "A souls like game",
                price,
                19,
                "http://image_url",
                "11",
                true
        );

        // when

        // then
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productDto);

        assertThat(violations.isEmpty()).isTrue();


    }

    @Test
    public void ProductDto_nullName_shouldNotValidate()
    {
        // given
        BigDecimal price = new BigDecimal("69.99");
        ProductRequest productDto = new ProductRequest(
                "19",
                null,
                "A souls like game",
                price,
                19,
                "http://image_url",
                "11",
                true
        );


        // when

        // then
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productDto);

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<ProductRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("name is required");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("name");
        assertThat(violation.getInvalidValue()).isEqualTo(null);

    }

    @Test
    public void ProductDto_sizeNameLessThan3_shouldNotValidate()
    {
        // given
        BigDecimal price = new BigDecimal("69.99");

        ProductRequest productDto = new ProductRequest(
                "19",
                "a1",
                "A souls like game",
                price,
                19,
                "http://image_url",
                "11",
                true
        );


        // when

        // then
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productDto);

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<ProductRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("name must be between 3 and 45 character");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("name");
        assertThat(violation.getInvalidValue()).isEqualTo("a1");
    }

    @Test
    public void ProductDto_sizeNameGreaterThan45_shouldNotValidate()
    {
        // given
        BigDecimal price = new BigDecimal("69.99");

        ProductRequest productDto = new ProductRequest(
                "19",
                "asdf asdf asdfg asdf asdfg asdfg asdfg asdfg asdfg",
                "A souls like game",
                price,
                19,
                "http://image_url",
                "11",
                true
        );


        // when

        // then
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productDto);

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<ProductRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("name must be between 3 and 45 character");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("name");
        assertThat(violation.getInvalidValue()).isEqualTo("asdf asdf asdfg asdf asdfg asdfg asdfg asdfg asdfg");
    }

    @Test
    public void ProductDto_nameStartWithNumberAndContainsSpecialCharacter_shouldNotValidate()
    {
        // given
        BigDecimal price = new BigDecimal("69.99");

        ProductRequest productDto = new ProductRequest(
                "19",
                "1@a",
                "A souls like game",
                price,
                19,
                "http://image_url",
                "11",
                true
        );


        // when

        // then
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productDto);

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<ProductRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("name should only contain chars/digits");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("name");
        assertThat(violation.getInvalidValue()).isEqualTo("1@a");
    }

    @Test
    public void ProductDto_nullDescription_shouldNotValidate()
    {
        // given
        BigDecimal price = new BigDecimal("69.99");

        ProductRequest productDto = new ProductRequest(
                "19",
                "Bloodborne",
                null,
                price,
                19,
                "http://image_url",
                "11",
                true
        );

        // when

        // then
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productDto);

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<ProductRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("description is required");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("description");
        assertThat(violation.getInvalidValue()).isEqualTo(null);

    }

    @Test
    public void ProductDto_descriptionSpecialCharacters_shouldNotValidate()
    {
        // given
        BigDecimal price = new BigDecimal("69.99");

        ProductRequest productDto = new ProductRequest(
                "19",
                "Bloodborne",
                "@$!@#$ fasdfj asdf",
                price,
                19,
                "http://image_url",
                "11",
                true
        );


        // when

        // then
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productDto);

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<ProductRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("description should only contain chars/digits");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("description");
        assertThat(violation.getInvalidValue()).isEqualTo("@$!@#$ fasdfj asdf");
    }

    @Test
    public void ProductDto_priceNull_shouldNotValidate()
    {
        // given

        ProductRequest productDto = new ProductRequest(
                "19",
                "Bloodborne",
                "A souls like game",
                null,
                19,
                "http://image_url",
                "11",
                true
        );

        // when

        // then
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productDto);

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<ProductRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("price is required");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("price");
        assertThat(violation.getInvalidValue()).isEqualTo(null);
    }

    @Test
    public void ProductDto_priceLessOrEqualTo0_shouldNotValidate()
    {
        // given
        ProductRequest productDto = new ProductRequest(
                "19",
                "Bloodborne",
                "A souls like game",
                new BigDecimal("0.00"),
                19,
                "http://image_url",
                "11",
                true
        );


        // when

        // then
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productDto);

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<ProductRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("price must be greater than 0.0");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("price");
        assertThat(violation.getInvalidValue()).isEqualTo(new BigDecimal("0.00"));
    }

    @Test
    public void ProductDto_priceGreaterThan5Integer_shouldNotValidate()
    {
        // given
        ProductRequest productDto = new ProductRequest(
                "19",
                "Bloodborne",
                "A souls like game",
                new BigDecimal("123456.12"),
                19,
                "http://image_url",
                "11",
                true
        );


        // when

        // then
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productDto);

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<ProductRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("price must be between 0.00 and 99999.99");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("price");
        assertThat(violation.getInvalidValue()).isEqualTo(new BigDecimal("123456.12"));
    }

    @Test
    public void ProductDto_priceGreaterThan2Fraction_shouldNotValidate()
    {
        // given
        ProductRequest productDto = new ProductRequest(
                "19",
                "Bloodborne",
                "A souls like game",
                new BigDecimal("12345.123"),
                19,
                "http://image_url",
                "11",
                true
        );


        // when

        // then
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productDto);

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<ProductRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("price must be between 0.00 and 99999.99");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("price");
        assertThat(violation.getInvalidValue()).isEqualTo(new BigDecimal("12345.123"));
    }

    @Test
    public void ProductDto_quantityNull_shouldNotValidate()
    {
        // given
        BigDecimal price = new BigDecimal("69.99");

        ProductRequest productDto = new ProductRequest(
                "19",
                "Bloodborne",
                "A souls like game",
                price,
                null,
                "http://image_url",
                "11",
                true
        );


        // when

        // then
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productDto);

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<ProductRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("quantity is required");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("quantity");
        assertThat(violation.getInvalidValue()).isEqualTo(null);
    }

    @Test
    public void ProductDto_quantityLessThan0_shouldNotValidate()
    {
        // given
        BigDecimal price = new BigDecimal("69.99");

        ProductRequest productDto = new ProductRequest(
                "19",
                "Bloodborne",
                "A souls like game",
                price,
                -1,
                "http://image_url",
                "11",
                true
        );

        // when

        // then
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productDto);

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<ProductRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("quantity must be greater than or equal to 0");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("quantity");
        assertThat(violation.getInvalidValue()).isEqualTo(-1);
    }

    @Test
    public void ProductDto_quantityGreaterThan1000_shouldNotValidate()
    {
        // given
        BigDecimal price = new BigDecimal("69.99");

        ProductRequest productDto = new ProductRequest(
                "19",
                "Bloodborne",
                "A souls like game",
                price,
                1001,
                "http://image_url",
                "11",
                true
        );

        // when

        // then
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productDto);

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<ProductRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("quantity must be less than or equal to 1000");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("quantity");
        assertThat(violation.getInvalidValue()).isEqualTo(1001);
    }

    @Test
    public void ProductDto_nullCategory_shouldNotValidate()
    {
        // given
        ProductRequest productDto = new ProductRequest(
                "19",
                "Bloodborne",
                "A souls like game",
                new BigDecimal("59.59"),
                19,
                "http://image_url",
                null,
                true
        );



        // when

        // then
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productDto);

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<ProductRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("category is required");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("categoryId");
        assertThat(violation.getInvalidValue()).isEqualTo(null);
    }

    @Test
    public void ProductDto_nullActive_shouldNotValidate()
    {
        // given

        ProductRequest productDto = new ProductRequest(
                "19",
                "Bloodborne",
                "A souls like game",
                new BigDecimal("59.59"),
                19,
                "http://image_url",
                "11",
                null
        );

        // when

        // then
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productDto);

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<ProductRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("active is required");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("active");
        assertThat(violation.getInvalidValue()).isEqualTo(null);
    }

}