package com.OnlineShop.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom phone number validation which is validated by custom validator:
 * {@link PhoneNumberConstraintValidator}
 *
 * <br>
 * <br>
 * <strong>param</strong>: countryCode an array of string number (string []) which are the phone code of countries
 *
 * <br>
 * <strong>param</strong>: phoneNumberLength an array of number representing the length of corresponding country codes phone number
 */
@Constraint(validatedBy = PhoneNumberConstraintValidator.class)
@Target( {ElementType.METHOD, ElementType.FIELD} )
@Retention(RetentionPolicy.RUNTIME)
public @interface PhoneNumber
{
    // define default course code
    String[] countryCode() default {"001"};
    int[] phoneNumberLength() default {13};

    // define default error message
    String message() default "Invalid phone number";

    // define default groups
    Class<?>[] groups() default {};

    // define default payloads
    Class<? extends Payload>[] payload() default {};
}
