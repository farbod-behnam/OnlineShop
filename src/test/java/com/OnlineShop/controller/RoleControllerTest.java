package com.OnlineShop.controller;

import com.OnlineShop.entity.AppRole;
import com.OnlineShop.enums.RoleEnum;
import com.OnlineShop.service.IRoleService;
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

    @Test
    void getRole_shouldReturnARole() throws Exception
    {
        // given

        AppRole role = new AppRole("19", RoleEnum.ROLE_USER.name());


        given(roleService.getRoleById(anyString())).willReturn(role);

        // when

        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/roles/" + "19"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(equalTo("19")))
                .andExpect(jsonPath("$.name").value(equalTo("ROLE_USER")))
                .andDo(print());
    }

    @Test
    void postRole_shouldReturnCreatedRole() throws Exception
    {
        // given

        AppRole role = new AppRole("19", RoleEnum.ROLE_USER.name());


        given(roleService.createRole(any(AppRole.class))).willReturn(role);

        // when

        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/roles/")
                .content(asJsonString(role))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(equalTo("19")))
                .andExpect(jsonPath("$.name").value(equalTo("ROLE_USER")))
                .andDo(print());
    }

    @Test
    void putRole_shouldReturnUpdatedRole() throws Exception
    {
        // given

        AppRole role = new AppRole("19", RoleEnum.ROLE_USER.name());


        given(roleService.updateRole(any(AppRole.class))).willReturn(role);

        // when

        // then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/roles/")
                        .content(asJsonString(role))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(equalTo("19")))
                .andExpect(jsonPath("$.name").value(equalTo("ROLE_USER")))
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