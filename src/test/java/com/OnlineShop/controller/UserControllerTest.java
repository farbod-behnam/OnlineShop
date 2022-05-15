package com.OnlineShop.controller;

import com.OnlineShop.entity.AppRole;
import com.OnlineShop.entity.AppUser;
import com.OnlineShop.entity.Country;
import com.OnlineShop.enums.CountryEnum;
import com.OnlineShop.enums.RoleEnum;
import com.OnlineShop.service.IUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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
        List<AppRole> roles = new ArrayList<>();

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
                "This is an address"
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
                "This is an address"
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
                .andExpect(jsonPath("$[0].address").value(equalTo("This is an address")));
    }
}