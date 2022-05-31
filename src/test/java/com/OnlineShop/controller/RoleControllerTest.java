package com.OnlineShop.controller;

import com.OnlineShop.entity.AppRole;
import com.OnlineShop.enums.RoleEnum;
import com.OnlineShop.security.service.ITokenService;
import com.OnlineShop.service.IRoleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
@WebMvcTest(controllers = RoleController.class)
//@Import(SecurityConfig.class)
class RoleControllerTest
{

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IRoleService roleService;

    // need to be mocked because SecurityConfig.class injects
    // these two services ( UserDetailsService, ITokenService ) into itself
    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private ITokenService tokenService;


    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    void getRoles_authorizedByAdmin_shouldReturnRoles() throws Exception
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
    @WithMockUser(authorities = {"ROLE_USER"})
    void getRoles_shouldBeUnauthorizedByUser() throws Exception
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
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    void getRoles_shouldBeUnauthorized() throws Exception
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
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    void getRole_authorizedByAdmin_shouldReturnARole() throws Exception
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
    @WithMockUser(authorities = {"ROLE_USER"})
    void getRole_shouldBeUnauthorizedByUser() throws Exception
    {
        // given

        AppRole role = new AppRole("19", RoleEnum.ROLE_USER.name());


        given(roleService.getRoleById(anyString())).willReturn(role);

        // when

        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/roles/" + "19"))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    void getRole_shouldBeUnauthorizedBy() throws Exception
    {
        // given

        AppRole role = new AppRole("19", RoleEnum.ROLE_USER.name());


        given(roleService.getRoleById(anyString())).willReturn(role);

        // when

        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/roles/" + "19"))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    void postRole_authorizedByAdmin_shouldReturnCreatedRole() throws Exception
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
    @WithMockUser(authorities = {"ROLE_USER"})
    void postRole_shouldBeUnauthorizedByUser() throws Exception
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
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    void postRole_shouldBeUnauthorized() throws Exception
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
                .andExpect(status().isForbidden())
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