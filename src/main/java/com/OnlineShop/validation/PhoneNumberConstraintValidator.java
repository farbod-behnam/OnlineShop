package com.OnlineShop.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Custom phone number validator
 */
public class PhoneNumberConstraintValidator implements ConstraintValidator<PhoneNumber, String>
{
    private String[] countryCode;
    private int[] phoneNumberLength;


    @Override
    public void initialize(PhoneNumber constraintAnnotation)
    {
        countryCode = constraintAnnotation.countryCode();
        phoneNumberLength = constraintAnnotation.phoneNumberLength();
    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context)
    {

        for (int i = 0, len = countryCode.length; i < len; i++)
        {
            if (phoneNumber.startsWith(countryCode[i]) && phoneNumber.length() == phoneNumberLength[i])
                return true;
        }

        return false;


    }
}
