package com.OnlineShop.service;

import com.OnlineShop.entity.Country;
import com.OnlineShop.enums.CountryEnum;
import com.OnlineShop.exception.NotFoundException;
import com.OnlineShop.repository.ICountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
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
    void getCountryById_shouldReturnACountry()
    {
        // given
        Country country = new Country("19", CountryEnum.Germany.toString());

        given(countryRepository.findById(anyString())).willReturn(Optional.of(country));

        // when
        Country foundCountry = underTestCountryService.getCountry(anyString());

        // then
        verify(countryRepository).findById(anyString());
        assertThat(foundCountry).isEqualTo(country);
    }

    @Test
    void getCountryById_shouldThrowNotFoundException()
    {
        // given
        String countryId = "12";

        // when

        // then
        assertThatThrownBy(() -> underTestCountryService.getCountry(countryId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Country with id: [" + countryId + "] cannot be found");
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