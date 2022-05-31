package com.OnlineShop.controller;

import com.OnlineShop.entity.Country;
import com.OnlineShop.enums.CountryEnum;
import com.OnlineShop.security.SecurityConfig;
import com.OnlineShop.security.service.ITokenService;
import com.OnlineShop.service.ICountryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CountryController.class)
//@Import(SecurityConfig.class)
class CountryControllerTest
{

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ICountryService countryService;

    // need to be mocked because SecurityConfig.class injects
    // these two services ( UserDetailsService, ITokenService ) into itself
    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private ITokenService tokenService;


    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN", "ROLE_USER"})
    void getCountries_authorizedByUserAndAdmin_shouldReturnCountryList() throws Exception
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

    @Test
    void getCountries_shouldBeUnauthorized() throws Exception
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
                .andExpect(status().isForbidden())
                .andDo(print());
    }


    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN", "ROLE_USER"})
    void getCountry_authorizedByUserAndAdmin_shouldReturnACountry() throws Exception
    {
        // given
        Country country = new Country("19", CountryEnum.Germany.name());



        given(countryService.getCountryById(anyString())).willReturn(country);

        // when

        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/countries" + "/19"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(equalTo("19")))
                .andExpect(jsonPath("$.name").value(equalTo("Germany")))
                .andDo(print());
    }


    @Test
    void getCountry_shouldBeUnauthorized() throws Exception
    {
        // given
        Country country = new Country("19", CountryEnum.Germany.name());



        given(countryService.getCountryById(anyString())).willReturn(country);

        // when

        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/countries" + "/19"))
                .andExpect(status().isForbidden())
                .andDo(print());
    }


    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    void postCountry_authorizedByAdmin_shouldReturnCreatedCountry() throws Exception
    {
        // given
        Country country = new Country("19", CountryEnum.Germany.toString());

        given(countryService.createCountry(any(Country.class))).willReturn(country);

        // when

        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/countries")
                .content(asJsonString(country))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(equalTo("19")))
                .andExpect(jsonPath("$.name").value(equalTo("germany")))
                .andDo(print());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    void postCountry_shouldBeUnauthorizedByUser() throws Exception
    {
        // given
        Country country = new Country("19", CountryEnum.Germany.toString());

        given(countryService.createCountry(any(Country.class))).willReturn(country);

        // when

        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/countries")
                        .content(asJsonString(country))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    void postCountry_shouldBeUnauthorized() throws Exception
    {
        // given
        Country country = new Country("19", CountryEnum.Germany.toString());

        given(countryService.createCountry(any(Country.class))).willReturn(country);

        // when

        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/countries")
                        .content(asJsonString(country))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    void putCountry_shouldReturnUpdatedCountry() throws Exception
    {
        // given
        Country country = new Country("19", CountryEnum.Germany.toString());

        given(countryService.updateCountry(any(Country.class))).willReturn(country);

        // when

        // then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/countries")
                        .content(asJsonString(country))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(equalTo("19")))
                .andExpect(jsonPath("$.name").value(equalTo("germany")))
                .andDo(print());
    }


    @Test
    public void deleteCountry_ShouldReturnString() throws Exception
    {
        // given
        String countryId = "11";

        // when

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/countries/" + countryId)
                        .content(asJsonString("Country with id: [" + countryId + "] is deleted"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(equalTo("Country with id: [" + countryId + "] is deleted")))
                .andDo(print());

    }

    private String asJsonString(final Object obj)
    {
        try
        {
            final ObjectMapper mapper = new ObjectMapper();

            return mapper.writeValueAsString(obj);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}