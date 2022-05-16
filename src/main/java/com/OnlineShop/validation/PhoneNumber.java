package com.OnlineShop.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
