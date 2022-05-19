package com.OnlineShop.service;

import com.OnlineShop.entity.AppRole;
import com.OnlineShop.entity.Country;
import com.OnlineShop.exception.AlreadyExistsException;
import com.OnlineShop.exception.NotFoundException;
import com.OnlineShop.repository.ICountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    public Country getCountry(String countryId)
    {
        Optional<Country> result = countryRepository.findById(countryId);

        Country country;

        if (result.isPresent())
            country = result.get();
        else
            throw new NotFoundException("Country with id: [" + countryId + "] cannot be found");

        return country;
    }

    @Override
    public Country createCountry(Country country)
    {
        // in order to create new entity
        country.setId(null);

        Optional<Country> result = countryRepository.findCountryByName(country.getName());

        if (result.isPresent())
            throw new AlreadyExistsException("Country name already exists.");


        return countryRepository.save(country);
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
