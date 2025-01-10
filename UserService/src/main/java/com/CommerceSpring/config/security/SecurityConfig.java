package com.CommerceSpring.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.CommerceSpring.constants.EndPoints.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;

    private static final String[] USER_WHITELIST = {
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/v3/api-docs/**",
            "/api/public/**",
            "/api/public/authenticate",
            "/actuator/*",
            "/swagger-ui/**",
            ROLE + CREATE_USER_ROLE + "/**",
            ROLE + UPDATE_USER_ROLE + "/**",
            ROLE + GET_ALL_USER_ROLES + "/**",
            USER + DELETE + "/**",
            USER + UPDATE + "/**",
            USER + SAVE + "/**",
            USER + "/get-all-users",
            USER + "/add-role-to-user",
            USER + "/get-user-roles"
    };


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorize->
                        authorize
                                .requestMatchers(USER_WHITELIST)
                                .permitAll()
                                //.requestMatchers("dev/v1/user/update-user").hasAnyAuthority("ADMIN","SUPER_ADMIN","UNASSIGNED")
                                //.requestMatchers("dev/v1/role/**","/dev/v1/user/**").hasAnyAuthority("ADMIN","SUPER_ADMIN")
                                .anyRequest().authenticated()
                );
        return httpSecurity.build();
    }
}
