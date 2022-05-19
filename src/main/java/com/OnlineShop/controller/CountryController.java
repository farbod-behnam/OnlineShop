package com.OnlineShop.controller;

import com.OnlineShop.entity.Country;
import com.OnlineShop.service.ICountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @GetMapping("/{countryId}")
    public ResponseEntity<Country> getCountry(@PathVariable String countryId)
    {
        Country country = countryService.getCountry(countryId);

        return new ResponseEntity<>(country, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Country> postCountry(@Valid @RequestBody Country country)
    {
        Country createdCountry = countryService.createCountry(country);

        return new ResponseEntity<>(createdCountry, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Country> putCountry(@Valid @RequestBody Country country)
    {
        Country updateCountry = countryService.updateCountry(country);

        return new ResponseEntity<>(updateCountry, HttpStatus.OK);
    }

    @DeleteMapping("/{countryId}")
    public ResponseEntity<String> deleteCountry(@PathVariable String countryId)
    {
        countryService.deleteCountryById(countryId);

        return new ResponseEntity<>("Country with id: [" + countryId + "] is deleted", HttpStatus.OK);
    }
}
