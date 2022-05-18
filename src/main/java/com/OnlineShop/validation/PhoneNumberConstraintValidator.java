package com.OnlineShop.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneNumberConstraintValidator implements ConstraintValidator<PhoneNumber, String>
{
    private String[] countryCode;
    private int[] phoneNumberLength;
//    private Map<String, Integer>  phoneNumberMap; // phoneNumberCountryCode and its length


    @Override
    public void initialize(PhoneNumber constraintAnnotation)
    {
        countryCode = constraintAnnotation.countryCode();
        phoneNumberLength = constraintAnnotation.phoneNumberLength();
//        phoneNumberMap = new HashMap<>();

//        for (int i = 0, len = countryCodeArray.length; i < len; i ++)
//        {
//            phoneNumberMap.put(countryCodeArray[i], phoneNumberLengthArray[i]);
//        }
//        phoneNumberMap.put("0049", 14); // Germany
//        phoneNumberMap.put("001", 13); // USA
//        phoneNumberMap.put("0044", 14); // UK
    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context)
    {


//        for (Map.Entry<String, Integer> entry : phoneNumberMap.entrySet())
//        {
//            if (phoneNumber.startsWith(entry.getKey()) && phoneNumber.length() == entry.getValue())
//            {
//                return true;
//            }
//        }

        for (int i = 0, len = countryCode.length; i < len; i++)
        {
            if (phoneNumber.startsWith(countryCode[i]) && phoneNumber.length() == phoneNumberLength[i])
                return true;
        }

        return false;


    }
}
