package com.OnlineShop.service;

import com.OnlineShop.entity.Country;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CountryService implements ICountryService
{
    @Override
    public List<Country> getCountries()
    {
        return null;
    }

    @Override
    public Country getCountryById(String countryId)
    {
        return null;
    }

    @Override
    public Country getCountryByName(String name)
    {
        return null;
    }

    @Override
    public Country createCountry(Country country)
    {
        return null;
    }

    @Override
    public Country updateCountry(Country country)
    {
        return null;
    }

    @Override
    public void deleteCountryById(String countryId)
    {

    }
}
