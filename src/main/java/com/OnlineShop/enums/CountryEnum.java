package com.OnlineShop.enums;

import java.util.Locale;

public enum CountryEnum
{
    Germany,
    UK,
    USA;

    @Override
    public String toString()
    {
        return name().toLowerCase(Locale.ROOT);
    }
}
