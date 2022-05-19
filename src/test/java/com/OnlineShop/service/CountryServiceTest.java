package com.OnlineShop.service;

import com.OnlineShop.repository.ICountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CountryServiceTest
{

    private ICountryService underTestCountryService;

    @Mock
    private ICountryRepository countryRepository;

    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.openMocks(this);
        underTestCountryService = new CountryService(countryRepository);
    }

    @Test
    void getCountries_shouldReturnCountryList()
    {
        // given

        // when
        underTestCountryService.getCountries();

        // then
        verify(countryRepository).findAll();
    }

    @Test
    void getCountryById()
    {
    }

    @Test
    void createCountry()
    {
    }

    @Test
    void updateCountry()
    {
    }

    @Test
    void deleteCountryById()
    {
    }
}