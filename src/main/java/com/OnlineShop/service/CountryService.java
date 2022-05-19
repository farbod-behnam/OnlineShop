package com.OnlineShop.service;

import com.OnlineShop.entity.Country;
import com.OnlineShop.repository.ICountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CountryService implements ICountryService
{

    private final ICountryRepository countryRepository;

    @Autowired
    public CountryService(ICountryRepository countryRepository)
    {
        this.countryRepository = countryRepository;
    }

    @Override
    public List<Country> getCountries()
    {
        return countryRepository.findAll();
    }

    @Override
    public Country getCountryById(String countryId)
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
