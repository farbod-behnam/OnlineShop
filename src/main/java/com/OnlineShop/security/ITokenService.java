package com.OnlineShop.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public interface ITokenService
{
    String createJWT(User user);
    void createDecodedJWT(String authorizationHeader);
    String getUsernameDecodedJWT();
    String[] getRolesDecodedJWT();
    Collection<SimpleGrantedAuthority> getAuthoritiesDecodedJWT();
}
