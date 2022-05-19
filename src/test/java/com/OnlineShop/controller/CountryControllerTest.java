package com.OnlineShop.controller;

import com.OnlineShop.entity.AppRole;
import com.OnlineShop.entity.Country;
import com.OnlineShop.enums.CountryEnum;
import com.OnlineShop.enums.RoleEnum;
import com.OnlineShop.service.ICountryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CountryController.class)
class CountryControllerTest
{

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ICountryService countryService;


    @Test
    void getCountries_shouldReturnCountryList() throws Exception
    {
        // given
        Country country1 = new Country("19", CountryEnum.Germany.name());
        Country country2 = new Country("20", CountryEnum.USA.name());

        List<Country> countries = new ArrayList<>();

        countries.add(country1);
        countries.add(country2);

        given(countryService.getCountries()).willReturn(countries);

        // when

        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/countries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(countries.size()))
                .andExpect(jsonPath("$[0].id").value(equalTo("19")))
                .andExpect(jsonPath("$[0].name").value(equalTo("Germany")))
                .andExpect(jsonPath("$[1].id").value(equalTo("20")))
                .andExpect(jsonPath("$[1].name").value(equalTo("USA")))
                .andDo(print());
    }


}