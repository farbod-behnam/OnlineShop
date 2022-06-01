package com.OnlineShop.security.service;

import com.OnlineShop.entity.AppUser;
import com.OnlineShop.security.userdetails.UserDetailsImpl;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;

/**
 * A class service to create JWT, or decode JWT
 */
public interface ITokenService
{
    /**
     * A method to create JWT string
     *
     * @param user {@link UserDetailsImpl} class
     * @return token - a JWT string created based on UserDetailsImpl, algorithm and secret
     */
    String createJWT(UserDetailsImpl user);


    /**
     * It creates {@link DecodedJWT} interface, which then can be used to call {@link ITokenService}
     * interface other methods regarding DecodedJWT.
     *
     * <br>
     * <br>
     * <strong>Note: </strong> this method should be called before
     * getUsernameDecodedJWT(),
     * getRolesDecodedJWT(),
     * getAuthoritiesDecodedJWT()
     *
     * @param authorizationHeader Http Request Authorization key value
     */
    void createDecodedJWT(String authorizationHeader);

    /**
     * it returns the username string which is decoded from JWT
     *
     * @return username - string
     *
     * <br>
     * <br>
     * <strong>Note: </strong>
     * call <strong>createDecodedJWT</strong> method before this method call
     */
    String getUsernameDecodedJWT();

    /**
     * it gets the "roles" claims from decoded JWT
     *
     * @return String[] - string array of role names
     *
     * <br>
     * <br>
     * <strong>Note: </strong>
     * call <strong>createDecodedJWT</strong> method before this method call
     */
    String[] getRolesDecodedJWT();

    /**
     * it iterates the string role name array and add them to a
     * collection of {@link SimpleGrantedAuthority}
     *
     * @return authorities - Collection<{@link SimpleGrantedAuthority}>
     *
     * <br>
     * <br>
     * <strong>Note: </strong>
     * call <strong>createDecodedJWT</strong> method before this method call
     */
    Collection<SimpleGrantedAuthority> getAuthoritiesDecodedJWT();
}
