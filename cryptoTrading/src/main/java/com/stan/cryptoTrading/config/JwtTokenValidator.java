package com.stan.cryptoTrading.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;

public class JwtTokenValidator extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String jwtToken = request.getHeader(JwtConstant.JWT_HEADER);

        // Bearer token
        if(jwtToken != null){
            jwtToken=jwtToken.substring(7);
            // Validate the JwtToken
            try{
                SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
                // information from the actual jwtToken
                Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwtToken).getBody();
                String email = String.valueOf(claims.get("EMAIL"));
                String authorities = String.valueOf(claims.get("authorities"));

                List<GrantedAuthority> authorityList = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);

                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        authorityList
                );

                //save the authentication to a security context holder
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }catch (Exception e){
                throw new RuntimeException("Invalid token provided...");
            }
        }
        filterChain.doFilter(request, response);
    }
}
