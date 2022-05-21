package com.OnlineShop.service;

import com.OnlineShop.entity.Country;

import java.util.List;

public interface ICountryService
{
    List<Country> getCountries();

    Country getCountryById(String countryId);

    Country createCountry(Country country);

    Country updateCountry(Country country);

    void deleteCountry(String countryId);
}
