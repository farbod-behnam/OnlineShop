package com.OnlineShop.security;

import com.OnlineShop.enums.RoleEnum;
import com.OnlineShop.filter.AuthTokenFilter;
import com.OnlineShop.filter.ExceptionHandlerFilter;
import com.OnlineShop.security.service.ITokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

import java.security.SecureRandom;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    private final UserDetailsService userDetailsService;
    private final ITokenService tokenService;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService, ITokenService tokenService)
    {
        this.userDetailsService = userDetailsService;
        this.tokenService = tokenService;
    }

    @Bean
    PasswordEncoder passwordEncoder()
    {
        int strength = 14;
        return new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2Y, strength, new SecureRandom());
    }

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter()
    {
        return new AuthTokenFilter(tokenService);
    }

    @Bean
    public ExceptionHandlerFilter exceptionHandlerFilter()
    {
        return new ExceptionHandlerFilter();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception
    {
        return super.authenticationManagerBean();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // first whatever url you want to allow without any authorization
        http.authorizeRequests().antMatchers("/api/auth/**").permitAll()
                .and()
                .authorizeRequests().antMatchers(HttpMethod.GET, "/api/categories/**").permitAll()
                .and()
                .authorizeRequests().antMatchers(HttpMethod.GET, "/api/products/**").permitAll()
                .and()
                .authorizeRequests().antMatchers(HttpMethod.GET, "/api/countries/**").permitAll()
                .and()
                // --------------------------------------------------
                // now urls that needs authorization
                // --------------------------------------------------
                // auth
                .authorizeRequests().antMatchers(HttpMethod.PUT, "/api/auth/update/**").hasAnyAuthority(RoleEnum.ROLE_USER.name(), RoleEnum.ROLE_ADMIN.name())
                .and()
                .authorizeRequests().antMatchers(HttpMethod.POST, "/api/auth/logout/**").hasAnyAuthority(RoleEnum.ROLE_USER.name(), RoleEnum.ROLE_ADMIN.name())
                .and()
                // categories
                .authorizeRequests().antMatchers(HttpMethod.POST, "/api/categories/**").hasAnyAuthority(RoleEnum.ROLE_ADMIN.name())
                .and()
                .authorizeRequests().antMatchers(HttpMethod.PUT, "/api/categories/**").hasAnyAuthority(RoleEnum.ROLE_ADMIN.name())
                .and()
                .authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/categories/**").hasAnyAuthority(RoleEnum.ROLE_ADMIN.name())
                .and()
                // countries
                .authorizeRequests().antMatchers(HttpMethod.POST, "/api/countries/**").hasAnyAuthority(RoleEnum.ROLE_ADMIN.name())
                .and()
                .authorizeRequests().antMatchers(HttpMethod.PUT, "/api/countries/**").hasAnyAuthority(RoleEnum.ROLE_ADMIN.name())
                .and()
                .authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/countries/**").hasAnyAuthority(RoleEnum.ROLE_ADMIN.name())
                .and()
                // products
                .authorizeRequests().antMatchers(HttpMethod.POST, "/api/products/**").hasAnyAuthority(RoleEnum.ROLE_ADMIN.name())
                .and()
                .authorizeRequests().antMatchers(HttpMethod.PUT, "/api/products/**").hasAnyAuthority(RoleEnum.ROLE_ADMIN.name())
                .and()
                .authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/products/**").hasAnyAuthority(RoleEnum.ROLE_ADMIN.name())
                .and()
                // roles
                .authorizeRequests().antMatchers(HttpMethod.GET, "/api/roles/**").hasAnyAuthority(RoleEnum.ROLE_ADMIN.name())
                .and()
                .authorizeRequests().antMatchers(HttpMethod.POST, "/api/roles/**").hasAnyAuthority(RoleEnum.ROLE_ADMIN.name())
                .and()
                .authorizeRequests().antMatchers(HttpMethod.PUT, "/api/roles/**").hasAnyAuthority(RoleEnum.ROLE_ADMIN.name())
                .and()
                .authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/roles/**").hasAnyAuthority(RoleEnum.ROLE_ADMIN.name())
                .and()
                // users
                .authorizeRequests().antMatchers(HttpMethod.GET, "/api/users/**").hasAnyAuthority(RoleEnum.ROLE_ADMIN.name())
                .and()
                .authorizeRequests().antMatchers(HttpMethod.POST, "/api/users/**").hasAnyAuthority(RoleEnum.ROLE_ADMIN.name())
                .and()
                .authorizeRequests().antMatchers(HttpMethod.PUT, "/api/users/**").hasAnyAuthority(RoleEnum.ROLE_ADMIN.name())
                .and()
                .authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/users/**").hasAnyAuthority(RoleEnum.ROLE_ADMIN.name())
                .and()
                // orders
                .authorizeRequests().antMatchers(HttpMethod.GET, "/api/orders/user/**").hasAnyAuthority(RoleEnum.ROLE_USER.name())
                .and()
                .authorizeRequests().antMatchers(HttpMethod.GET, "/api/orders/**").hasAnyAuthority(RoleEnum.ROLE_ADMIN.name())
                .and()
                .authorizeRequests().antMatchers(HttpMethod.POST, "/api/orders/**").hasAnyAuthority(RoleEnum.ROLE_USER.name());



        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        http.addFilterBefore(exceptionHandlerFilter(), LogoutFilter.class);
    }
}
