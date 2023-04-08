package ru.aasmc.catalogservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

// this annotation enables Spring MVC support for Spring Security
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorize -> authorize
                        // Allow users to fetch greetings and books without authentication
                        .mvcMatchers(HttpMethod.GET, "/", "/books/**")
                        .permitAll()
                        // any other request requires authentication and employee role
                        .anyRequest().hasRole("employee")
                )
                // Enables OAuth2 Resource Server support using the default configuration
                // based on JWT authentication
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                // Each request must include an Access Token, so there's no need to keep a user
                // session alive between requests. We want it to be stateless.
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Since the authentication strategy is stateless and doesn't involve a
                // browser based client, we can safely disable the CSRF protection.
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

    /**
     * Ensures that Spring Security associates a list of GrantedAuthority objects
     * with each authenticated user, and we can use them to defile authorization
     * policies.
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        // Converter that maps claims to GrantedAuthority objects.
        var jwtGrantedAuthoritiesConverter =
                new JwtGrantedAuthoritiesConverter();
        // Applies the "ROLE_" prefix to each user role
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        // extracts the list of roles from the "roles" claim
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");

        var jwtAuthenticationConverter =
                new JwtAuthenticationConverter();
        // Defines a strategy to convert a JWT. We'll only customize hot to build
        // granted authorities out of it.
        jwtAuthenticationConverter
                .setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

}
