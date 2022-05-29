package com.OnlineShop.security.service;

import com.OnlineShop.security.userdetails.UserDetailsImpl;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;

public interface ITokenService
{
    String createJWT(UserDetailsImpl user);
    void createDecodedJWT(String authorizationHeader);
    String getUsernameDecodedJWT();
    String[] getRolesDecodedJWT();
    Collection<SimpleGrantedAuthority> getAuthoritiesDecodedJWT();
}
