package com.OnlineShop.security;

import com.OnlineShop.exception.AlreadyExistsException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.security.SignatureException;
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
    public String createJWT(User user)
    {
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes());

        String token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .withIssuer(jwtIssuer)
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);

        return token;
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
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
//        catch (SignatureVerificationException e)
//        {
//            System.out.println(e.getMessage());
////            throw new SignatureVerificationException(algorithm);
//        }
//        catch (AlgorithmMismatchException e)
//        {
//            System.out.println(e.getMessage());
////            throw new SignatureVerificationException(algorithm);
//        }
//        catch (JWTDecodeException e)
//        {
//            System.out.println(e.getMessage());
////            throw new JWTDecodeException("The string doesn't have a valid JSON format.");
//        }
//        catch (JWTVerificationException e)
//        {
//            System.out.println(e.getMessage());
//        }


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