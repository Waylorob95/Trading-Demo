package com.stan.cryptoTrading.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class JwtProvider {
    private static SecretKey secretKey = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

    // method for generating jwt token
    public static String generateToken(Authentication authentication){
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String roles = populateAuthorities(authorities);

        //create the token with 4 hours lifespan
        String jwt = Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 86400000))
                .claim("EMAIL", authentication.getName())
                .claim("authorities", roles)
                .signWith(secretKey)
                .compact();

        return jwt;
    }

    //access the EMAIL with providing the jwtToken
    public static String getEmailFromJwtToken(String token) {
        token = token.substring(7);
        Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
        String email = String.valueOf(claims.get("EMAIL"));

        return email;
    }

    // method for populating the roles
    private static String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<String> auth = new HashSet<>();
        for(GrantedAuthority ga : authorities){
            auth.add(ga.getAuthority());
        }
        return String.join(",", auth);
    }
}
