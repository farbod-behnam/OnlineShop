package com.OnlineShop.controller;

import com.OnlineShop.entity.Country;
import com.OnlineShop.service.ICountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/countries")
public class CountryController
{
    private final ICountryService countryService;

    @Autowired
    public CountryController(ICountryService countryService)
    {
        this.countryService = countryService;
    }

    @GetMapping
    public ResponseEntity<List<Country>> getCountries()
    {
        List<Country> countries = countryService.getCountries();

        return new ResponseEntity<>(countries, HttpStatus.OK);
    }
}
