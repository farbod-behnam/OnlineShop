package com.OnlineShop.controller;

import com.OnlineShop.entity.*;
import com.OnlineShop.enums.CountryEnum;
import com.OnlineShop.enums.RoleEnum;
import com.OnlineShop.service.IUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
class UserControllerTest
{

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IUserService userService;



    @Test
    void getUsers_shouldReturnUsers() throws Exception
    {
        // given
        List<AppUser> users = new ArrayList<>();
        Set<AppRole> roles = new HashSet<>();

        Country country = new Country("10", CountryEnum.Germany.name());
        AppRole role = new AppRole("11", RoleEnum.ROLE_USER.name());

        roles.add(role);

        AppUser user1 = new AppUser(
                "19",
                "John",
                "Wick",
                "001666666666",
                "john.wick@gmail.com",
                roles,
                "john.wick",
                "password1234",
                country,
                "This is an address",
                LocalDateTime.now(),
                LocalDateTime.now()
        );


        AppUser user2 = new AppUser(
                "20",
                "Peter",
                "Parker",
                "00119191919",
                "peter.parker@gmail.com",
                roles,
                "peter.parker",
                "password1234",
                country,
                "This is an address",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        users.add(user1);
        users.add(user2);

        given(userService.getUsers()).willReturn(users);
        // when

        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(users.size()))
                .andExpect(jsonPath("$[0].id").value(equalTo("19")))
                .andExpect(jsonPath("$[0].firstName").value(equalTo("John")))
                .andExpect(jsonPath("$[0].lastName").value(equalTo("Wick")))
                .andExpect(jsonPath("$[0].username").value(equalTo("john.wick")))
                .andExpect(jsonPath("$[0].phoneNumber").value(equalTo("001666666666")))
                .andExpect(jsonPath("$[0].email").value(equalTo("john.wick@gmail.com")))
                .andExpect(jsonPath("$[0].roles[0].name").value(equalTo(RoleEnum.ROLE_USER.name())))
                .andExpect(jsonPath("$[0].country.id").value(equalTo("10")))
                .andExpect(jsonPath("$[0].country.name").value(equalTo(CountryEnum.Germany.name())))
                .andExpect(jsonPath("$[0].address").value(equalTo("This is an address")))
                .andDo(print());
    }

    @Test
    void getUser_shouldReturnAUser() throws Exception
    {
        // given
        Set<AppRole> roles = new HashSet<>();

        Country country = new Country("10", CountryEnum.Germany.name());
        AppRole role = new AppRole("11", RoleEnum.ROLE_USER.name());

        roles.add(role);

        AppUser user = new AppUser(
                "19",
                "John",
                "Wick",
                "001666666666",
                "john.wick@gmail.com",
                roles,
                "john.wick",
                "password1234",
                country,
                "This is an address",
                LocalDateTime.now(),
                LocalDateTime.now()
        );


        given(userService.getUserById(anyString())).willReturn(user);

        // when

        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/" + "19"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(equalTo("19")))
                .andExpect(jsonPath("$.firstName").value(equalTo("John")))
                .andExpect(jsonPath("$.lastName").value(equalTo("Wick")))
                .andExpect(jsonPath("$.username").value(equalTo("john.wick")))
                .andExpect(jsonPath("$.phoneNumber").value(equalTo("001666666666")))
                .andExpect(jsonPath("$.email").value(equalTo("john.wick@gmail.com")))
                .andExpect(jsonPath("$.roles[0].name").value(equalTo(RoleEnum.ROLE_USER.name())))
                .andExpect(jsonPath("$.country.id").value(equalTo("10")))
                .andExpect(jsonPath("$.country.name").value(equalTo(CountryEnum.Germany.name())))
                .andExpect(jsonPath("$.address").value(equalTo("This is an address")))
                .andDo(print());
    }

    @Test
    public void postUser_shouldReturnCreatedUser() throws Exception
    {
        // given
        Set<AppRole> roles = new HashSet<>();

        Country country = new Country("10", CountryEnum.Germany.name());
        AppRole role = new AppRole("11", RoleEnum.ROLE_USER.name());

        roles.add(role);

        AppUser user = new AppUser(
                "19",
                "John",
                "Wick",
                "001666666666",
                "john.wick@gmail.com",
                roles,
                "john.wick",
                "password1234",
                country,
                "This is an address",
                LocalDateTime.now(),
                LocalDateTime.now()
        );


        given(userService.createUser(any(AppUser.class))).willReturn(user);

        // when

        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .content(asJsonString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(equalTo("19")))
                .andExpect(jsonPath("$.firstName").value(equalTo("John")))
                .andExpect(jsonPath("$.lastName").value(equalTo("Wick")))
                .andExpect(jsonPath("$.username").value(equalTo("john.wick")))
                .andExpect(jsonPath("$.phoneNumber").value(equalTo("001666666666")))
                .andExpect(jsonPath("$.email").value(equalTo("john.wick@gmail.com")))
                .andExpect(jsonPath("$.roles[0].name").value(equalTo(RoleEnum.ROLE_USER.name())))
                .andExpect(jsonPath("$.country.id").value(equalTo("10")))
                .andExpect(jsonPath("$.country.name").value(equalTo(CountryEnum.Germany.name())))
                .andExpect(jsonPath("$.address").value(equalTo("This is an address")))
                .andDo(print());

    }

    @Test
    public void putUser_shouldReturnUpdatedUser() throws Exception
    {
        // given
        Set<AppRole> roles = new HashSet<>();

        Country country = new Country("10", CountryEnum.Germany.name());
        AppRole role = new AppRole("11", RoleEnum.ROLE_USER.name());

        roles.add(role);

        AppUser user = new AppUser(
                "19",
                "John",
                "Wick",
                "001666666666",
                "john.wick@gmail.com",
                roles,
                "john.wick",
                "password1234",
                country,
                "This is an address",
                LocalDateTime.now(),
                LocalDateTime.now()
        );


        given(userService.updateUser(any(AppUser.class))).willReturn(user);

        // when

        // then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/users")
                        .content(asJsonString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(equalTo("19")))
                .andExpect(jsonPath("$.firstName").value(equalTo("John")))
                .andExpect(jsonPath("$.lastName").value(equalTo("Wick")))
                .andExpect(jsonPath("$.username").value(equalTo("john.wick")))
                .andExpect(jsonPath("$.phoneNumber").value(equalTo("001666666666")))
                .andExpect(jsonPath("$.email").value(equalTo("john.wick@gmail.com")))
                .andExpect(jsonPath("$.roles[0].name").value(equalTo(RoleEnum.ROLE_USER.name())))
                .andExpect(jsonPath("$.country.id").value(equalTo("10")))
                .andExpect(jsonPath("$.country.name").value(equalTo(CountryEnum.Germany.name())))
                .andExpect(jsonPath("$.address").value(equalTo("This is an address")))
                .andDo(print());

    }

    @Test
    public void deleteUser_ShouldReturnString() throws Exception
    {
        // given
        String userId = "11";

        // when

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/" + userId)
                        .content(asJsonString("User with id: [" + userId + "] is deleted"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(equalTo("User with id: [" + userId + "] is deleted")))
                .andDo(print());

    }

    private String asJsonString(final Object obj)
    {
        try
        {
            final ObjectMapper mapper = new ObjectMapper()
                    .registerModule(new JavaTimeModule()); // add jackson support for conversion of Date Time

            return mapper.writeValueAsString(obj);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}