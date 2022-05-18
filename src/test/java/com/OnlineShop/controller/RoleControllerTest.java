package com.OnlineShop.controller;

import com.OnlineShop.entity.AppRole;
import com.OnlineShop.enums.CountryEnum;
import com.OnlineShop.enums.RoleEnum;
import com.OnlineShop.service.IRoleService;
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
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RoleController.class)
class RoleControllerTest
{

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IRoleService roleService;

    @Test
    void getRoles_shouldReturnRoles() throws Exception
    {
        // given
        List<AppRole> roles = new ArrayList<>();

        AppRole role1 = new AppRole("19", RoleEnum.ROLE_USER.name());
        AppRole role2 = new AppRole("20", RoleEnum.ROLE_ADMIN.name());

        roles.add(role1);
        roles.add(role2);

        given(roleService.getRoles()).willReturn(roles);

        // when

        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(roles.size()))
                .andExpect(jsonPath("$[0].id").value(equalTo("19")))
                .andExpect(jsonPath("$[0].name").value(equalTo("ROLE_USER")))
                .andExpect(jsonPath("$[1].id").value(equalTo("20")))
                .andExpect(jsonPath("$[1].name").value(equalTo("ROLE_ADMIN")))
                .andDo(print());
    }


}