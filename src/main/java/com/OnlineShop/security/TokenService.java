package com.OnlineShop.security;

import com.OnlineShop.security.userdetails.UserDetailsImpl;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.*;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class TokenService implements ITokenService
{
    @Value("${onlineshop.app.jwt.secret}")
    private String jwtSecret;

    @Value("${onlineshop.app.jwt.expirationMs}")
    private int jwtExpirationMs;

    @Value("${onlineshop.app.jwt.issuer}")
    private String jwtIssuer;

    private DecodedJWT decodedJWT;


    @Override
    public String createJWT(UserDetailsImpl user)
    {
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes());

        try
        {
            String token = JWT.create()
                    .withSubject(user.getUsername())
                    .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpirationMs))
                    .withIssuer(jwtIssuer)
                    .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                    .sign(algorithm);

            return token;
        }
        catch (IllegalArgumentException e)
        {
            throw new IllegalArgumentException("the jwt authorities does not validate");
        }
        catch (JWTCreationException e)
        {
            throw new JWTCreationException("claims could not be converted to a valid JSON or there is a problem with the signing key.", new IllegalArgumentException("argument is not valid"));
        }

    }

    @Override
    public void createDecodedJWT(String authorizationHeader)
    {

        Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes());

        try
        {
            String token = authorizationHeader.substring("Bearer ".length());
            JWTVerifier verifier = JWT.require(algorithm).build();
            decodedJWT = verifier.verify(token);
        }
        catch (StringIndexOutOfBoundsException e)
        {
            throw new StringIndexOutOfBoundsException("token is out of bounds");
        }
        catch (AlgorithmMismatchException e)
        {
            throw new SignatureVerificationException(algorithm);
        }
        catch (InvalidClaimException e)
        {
            throw new InvalidClaimException("token claims are invalid");
        }
        catch (SignatureVerificationException e)
        {
            throw new SignatureVerificationException(algorithm);
        }
        catch (JWTDecodeException e)
        {
            throw new JWTDecodeException("The string doesn't have a valid JSON format.");
        }
        catch (TokenExpiredException e)
        {
            throw new TokenExpiredException("token is expired");
        }
        catch (JWTVerificationException e)
        {
            throw new JWTVerificationException("token cannot be verified");
        }


    }

    @Override
    public String getUsernameDecodedJWT()
    {
        return decodedJWT.getSubject();
    }

    @Override
    public String[] getRolesDecodedJWT()
    {
        return decodedJWT.getClaim("roles").asArray(String.class);
    }

    @Override
    public Collection<SimpleGrantedAuthority> getAuthoritiesDecodedJWT()
    {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        String[] roles = getRolesDecodedJWT();

        for (String role: roles)
            authorities.add(new SimpleGrantedAuthority(role));

        return authorities;
    }
}
