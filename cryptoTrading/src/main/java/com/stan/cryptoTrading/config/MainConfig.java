package com.stan.cryptoTrading.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
public class MainConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.sessionManagement(management->management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //authenticate endpoint with jwt token
                .authorizeHttpRequests(Authorize->Authorize.requestMatchers("/api/**").authenticated()
                        //check if the jwt token is valid or not(throw an error)
                        .anyRequest().permitAll()).addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class)
                //disable the cross-site request forgery(forces authentication of already authenticated users)
                .csrf(csrf->csrf.disable())
                //cross-origin resource sharing(front-end and back-end)
                .cors(cors->cors.configurationSource(corsConfigurationSource()));
        return http.build();
    }

    private CorsConfigurationSource corsConfigurationSource() {
        return null;
    }
}
