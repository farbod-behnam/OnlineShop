package com.OnlineShop.entity;

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
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ProductTest
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
    public void product_validProduct()
    {
        // given
        BigDecimal price = new BigDecimal("69.99");

        Product product = new Product(
                "19",
                "Bloodborne",
                "A souls like game",
                price,
                19,
                "http://image_url",
                new Category("11", "Video Games"),
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // when

        // then
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        assertThat(violations.isEmpty()).isTrue();


    }

    @Test
    public void product_nullName_shouldNotValidate()
    {
        // given
        BigDecimal price = new BigDecimal("69.99");

        Product product = new Product(
                "19",
                null,
                "A souls like game",
                price,
                19,
                "http://image_url",
                new Category("11", "Video Games"),
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // when

        // then
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<Product> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("name is required");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("name");
        assertThat(violation.getInvalidValue()).isEqualTo(null);

    }

    @Test
    public void product_sizeNameLessThan3_shouldNotValidate()
    {
        // given
        BigDecimal price = new BigDecimal("69.99");

        Product product = new Product(
                "19",
                "a1",
                "A souls like game",
                price,
                19,
                "http://image_url",
                new Category("11", "Video Games"),
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // when

        // then
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<Product> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("name must be between 3 and 45 character");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("name");
        assertThat(violation.getInvalidValue()).isEqualTo("a1");
    }

    @Test
    public void product_sizeNameGreaterThan45_shouldNotValidate()
    {
        // given
        BigDecimal price = new BigDecimal("69.99");

        Product product = new Product(
                "19",
                "asdf asdf asdfg asdf asdfg asdfg asdfg asdfg asdfg",
                "A souls like game",
                price,
                19,
                "http://image_url",
                new Category("11", "Video Games"),
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // when

        // then
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<Product> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("name must be between 3 and 45 character");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("name");
        assertThat(violation.getInvalidValue()).isEqualTo("asdf asdf asdfg asdf asdfg asdfg asdfg asdfg asdfg");
    }

    @Test
    public void product_nameStartWithNumberAndContainsSpecialCharacter_shouldNotValidate()
    {
        // given
        BigDecimal price = new BigDecimal("69.99");

        Product product = new Product(
                "19",
                "1@a",
                "A souls like game",
                price,
                19,
                "http://image_url",
                new Category("11", "Video Games"),
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // when

        // then
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<Product> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("name should only contain chars/digits");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("name");
        assertThat(violation.getInvalidValue()).isEqualTo("1@a");
    }

    @Test
    public void product_nullDescription_shouldNotValidate()
    {
        // given
        BigDecimal price = new BigDecimal("69.99");

        Product product = new Product(
                "19",
                "Bloodborne",
                null,
                price,
                19,
                "http://image_url",
                new Category("11", "Video Games"),
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // when

        // then
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<Product> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("description is required");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("description");
        assertThat(violation.getInvalidValue()).isEqualTo(null);

    }

    @Test
    public void product_descriptionSpecialCharacters_shouldNotValidate()
    {
        // given
        BigDecimal price = new BigDecimal("69.99");

        Product product = new Product(
                "19",
                "Bloodborne",
                "@$!@#$ fasdfj asdf",
                price,
                19,
                "http://image_url",
                new Category("11", "Video Games"),
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // when

        // then
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<Product> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("description should only contain chars/digits");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("description");
        assertThat(violation.getInvalidValue()).isEqualTo("@$!@#$ fasdfj asdf");
    }

    @Test
    public void product_priceNull_shouldNotValidate()
    {
        // given

        Product product = new Product(
                "19",
                "Bloodborne",
                "A souls like game",
                null,
                19,
                "http://image_url",
                new Category("11", "Video Games"),
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // when

        // then
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<Product> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("price is required");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("price");
        assertThat(violation.getInvalidValue()).isEqualTo(null);
    }

    @Test
    public void product_priceLessOrEqualTo0_shouldNotValidate()
    {
        // given

        Product product = new Product(
                "19",
                "Bloodborne",
                "A souls like game",
                new BigDecimal("0.00"),
                19,
                "http://image_url",
                new Category("11", "Video Games"),
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // when

        // then
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<Product> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("price must be greater than 0.0");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("price");
        assertThat(violation.getInvalidValue()).isEqualTo(new BigDecimal("0.00"));
    }

    @Test
    public void product_priceGreaterThan5Integer_shouldNotValidate()
    {
        // given

        Product product = new Product(
                "19",
                "Bloodborne",
                "A souls like game",
                new BigDecimal("123456.12"),
                19,
                "http://image_url",
                new Category("11", "Video Games"),
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // when

        // then
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<Product> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("price must be between 0.00 and 99999.99");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("price");
        assertThat(violation.getInvalidValue()).isEqualTo(new BigDecimal("123456.12"));
    }

    @Test
    public void product_priceGreaterThan2Fraction_shouldNotValidate()
    {
        // given

        Product product = new Product(
                "19",
                "Bloodborne",
                "A souls like game",
                new BigDecimal("12345.123"),
                19,
                "http://image_url",
                new Category("11", "Video Games"),
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // when

        // then
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<Product> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("price must be between 0.00 and 99999.99");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("price");
        assertThat(violation.getInvalidValue()).isEqualTo(new BigDecimal("12345.123"));
    }

    @Test
    public void product_quantityNull_shouldNotValidate()
    {
        // given

        Product product = new Product(
                "19",
                "Bloodborne",
                "A souls like game",
                new BigDecimal("59.59"),
                null,
                "http://image_url",
                new Category("11", "Video Games"),
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // when

        // then
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<Product> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("quantity is required");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("quantity");
        assertThat(violation.getInvalidValue()).isEqualTo(null);
    }

    @Test
    public void product_quantityLessThan0_shouldNotValidate()
    {
        // given

        Product product = new Product(
                "19",
                "Bloodborne",
                "A souls like game",
                new BigDecimal("59.59"),
                -1,
                "http://image_url",
                new Category("11", "Video Games"),
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // when

        // then
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<Product> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("quantity must be greater than or equal to 0");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("quantity");
        assertThat(violation.getInvalidValue()).isEqualTo(-1);
    }

    @Test
    public void product_quantityGreaterThan1000_shouldNotValidate()
    {
        // given

        Product product = new Product(
                "19",
                "Bloodborne",
                "A souls like game",
                new BigDecimal("59.59"),
                1001,
                "http://image_url",
                new Category("11", "Video Games"),
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // when

        // then
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<Product> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("quantity must be less than or equal to 1000");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("quantity");
        assertThat(violation.getInvalidValue()).isEqualTo(1001);
    }

    @Test
    public void product_nullCategory_shouldNotValidate()
    {
        // given

        Product product = new Product(
                "19",
                "Bloodborne",
                "A souls like game",
                new BigDecimal("59.59"),
                1000,
                "http://image_url",
                null,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // when

        // then
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<Product> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("category is required");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("category");
        assertThat(violation.getInvalidValue()).isEqualTo(null);
    }

    @Test
    public void product_nullActive_shouldNotValidate()
    {
        // given

        Product product = new Product(
                "19",
                "Bloodborne",
                "A souls like game",
                new BigDecimal("59.59"),
                1000,
                "http://image_url",
                new Category("11", "Video Games"),
                null,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // when

        // then
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<Product> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("active is required");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("active");
        assertThat(violation.getInvalidValue()).isEqualTo(null);
    }
}