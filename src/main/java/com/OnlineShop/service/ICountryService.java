package com.OnlineShop.service;

import com.OnlineShop.entity.Country;

import java.util.List;

public interface ICountryService
{
    List<Country> getCountries();

    Country getCountry(String countryId);

    Country createCountry(Country country);

    Country updateCountry(Country country);

    void deleteCountryById(String countryId);
}
