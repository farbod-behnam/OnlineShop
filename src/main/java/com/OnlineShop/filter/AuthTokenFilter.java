package com.OnlineShop.filter;

import com.OnlineShop.security.service.ITokenService;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

/**
 * A filter class which uses ITokenService and extends OncePerRequestFilter
 * to decode the JWT inside the http header for Authorization key
 */
public class AuthTokenFilter extends OncePerRequestFilter
{
    private final ITokenService tokenService;

    public AuthTokenFilter(ITokenService tokenService)
    {
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        if (request.getServletPath().equals("/api/auth/login"))
            filterChain.doFilter(request, response);
        else
        {
            String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer "))
            {
                tokenService.createDecodedJWT(authorizationHeader);
                String username = tokenService.getUsernameDecodedJWT();
                Collection<SimpleGrantedAuthority> authorities = tokenService.getAuthoritiesDecodedJWT();

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                filterChain.doFilter(request, response);
            }
            else
            {
                filterChain.doFilter(request, response);
            }
        }
    }
}
