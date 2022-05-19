package com.OnlineShop.service;

import com.OnlineShop.entity.Country;

import java.util.List;

public interface ICountryService
{
    List<Country> getCountries();

    Country getCountryById(String countryId);

    Country getCountryByName(String name);

    Country createCountry(Country country);

    Country updateCountry(Country country);

    void deleteCountryById(String countryId);
}
