package com.OnlineShop.service;

import com.OnlineShop.entity.Country;
import com.OnlineShop.enums.CountryEnum;
import com.OnlineShop.exception.AlreadyExistsException;
import com.OnlineShop.exception.NotFoundException;
import com.OnlineShop.repository.ICountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
    void createCountry_shouldReturnCreatedRole()
    {
        // given
        Country country = new Country("19", CountryEnum.Germany.toString());

        given(countryRepository.findCountryByName(anyString())).willReturn(Optional.empty());

        // when
        underTestCountryService.createCountry(country);

        // then
        ArgumentCaptor<Country> countryArgumentCaptor = ArgumentCaptor.forClass(Country.class);
        verify(countryRepository).findCountryByName(country.getName());
        verify(countryRepository).save(countryArgumentCaptor.capture());
        Country capturedCountry = countryArgumentCaptor.getValue();
        assertThat(capturedCountry).isEqualTo(country);
    }

    @Test
    void createCountry_shouldThrowAlreadyExistsException()
    {
        // given
        Country country = new Country("19", CountryEnum.Germany.toString());

        given(countryRepository.findCountryByName(anyString())).willReturn(Optional.of(country));

        Country countryTobeCreated = new Country( null, CountryEnum.Germany.toString());

        // when

        // then
        assertThatThrownBy(() -> underTestCountryService.createCountry(countryTobeCreated))
                .isInstanceOf(AlreadyExistsException.class)
                .hasMessageContaining("Country name already exists.");
    }

    @Test
    void updateCountry_shouldReturnUpdatedRole()
    {
        // given
        Country roleToBeUpdated = new Country("19", CountryEnum.Germany.toString());

        given(countryRepository.findCountryByName(roleToBeUpdated.getName())).willReturn(Optional.empty());
        given(countryRepository.findById(anyString())).willReturn(Optional.of(roleToBeUpdated));

        // when
        underTestCountryService.updateCountry(roleToBeUpdated);

        // then
        ArgumentCaptor<Country> countryArgumentCaptor = ArgumentCaptor.forClass(Country.class);
        verify(countryRepository).findCountryByName(roleToBeUpdated.getName());
        verify(countryRepository).save(countryArgumentCaptor.capture());
        Country capturedCountry = countryArgumentCaptor.getValue();
        assertThat(capturedCountry).isEqualTo(roleToBeUpdated);
    }

    @Test
    void updateCountry_shouldThrowAlreadyExistsException()
    {
        // given
        Country countryToBeUpdated = new Country("19", CountryEnum.Germany.toString());

        given(countryRepository.findCountryByName(countryToBeUpdated.getName())).willReturn(Optional.of(countryToBeUpdated));
//        given(countryRepository.findById(anyString())).willReturn(Optional.of(countryToBeUpdated));

        // when

        // then
        assertThatThrownBy(() -> underTestCountryService.updateCountry(countryToBeUpdated))
                .isInstanceOf(AlreadyExistsException.class)
                .hasMessageContaining("Country with name: [" + countryToBeUpdated.getName() + "] already exists.");
    }

    @Test
    void updateCountry_shouldThrowNotFoundException()
    {
        // given
        Country countryToBeUpdated = new Country("19", CountryEnum.Germany.toString());

        given(countryRepository.findCountryByName(countryToBeUpdated.getName())).willReturn(Optional.empty());
        given(countryRepository.findById(anyString())).willReturn(Optional.empty());

        // when

        // then
        assertThatThrownBy(() -> underTestCountryService.updateCountry(countryToBeUpdated))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Country with id: [" + countryToBeUpdated.getId() + "] cannot be found");
    }

    @Test
    void deleteCountry_shouldDeleteACountry()
    {
        // given
        Country deleteCountry = new Country("10", CountryEnum.UK.toString());
        given(countryRepository.findById(anyString())).willReturn(Optional.of(deleteCountry));

        // when
        underTestCountryService.deleteCountry(deleteCountry.getId());

        // then
        verify(countryRepository).deleteById(deleteCountry.getId());

    }

    @Test
    void deleteCountry_shouldThrowNotFoundException()
    {
        // given
        String countryId = "11";
        given(countryRepository.findById(anyString())).willReturn(Optional.empty());

        // when

        // then
        assertThatThrownBy(() -> underTestCountryService.deleteCountry(countryId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Country with id: [" + countryId + "] cannot be found");

    }
}