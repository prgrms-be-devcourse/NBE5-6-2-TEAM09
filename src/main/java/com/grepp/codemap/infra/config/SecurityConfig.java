package com.grepp.codemap.infra.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

//    @Value("${remember-me.key}")
//    private String rememberMeKey;


//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
//        return http.getSharedObject(AuthenticationManagerBuilder.class)
//            .build();
//    }
//
//
//    @Bean
//    public AuthenticationSuccessHandler successHandler() {
//        return new AuthenticationSuccessHandler() {
//            @Override
//            public void onAuthenticationSuccess(HttpServletRequest request,
//                HttpServletResponse response, Authentication authentication)
//                throws IOException, ServletException {
//
//                boolean isAdmin = authentication.getAuthorities()
//                    .stream()
//                    .anyMatch(authority ->
//                        authority.getAuthority().equals("ROLE_ADMIN"));
//
//                if (isAdmin) {
//                    response.sendRedirect("/admin/manage-members");
//                    return;
//                }
//
//                response.sendRedirect("/routines");
//            }
//        };
//    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .authorizeHttpRequests(
                (requests) -> requests
                    .requestMatchers(GET,"/", "/user/signup", "/user/signup/*", "/user/signin","/css/**", "/js/**", "/img/**")
                    .permitAll()
                    .requestMatchers(POST, "/user/signin", "/user/signup").permitAll()
                    .requestMatchers(POST, "/chatbot/**", "/chatbot/message/**").permitAll()
                    .anyRequest().authenticated()
            )
            //.rememberMe(rememberMe -> rememberMe.key(rememberMeKey))
            .logout(LogoutConfigurer::permitAll)
            .csrf(csrf -> csrf.ignoringRequestMatchers("/chatbot/**", "/chatbot/message/**"))
            .sessionManagement(session -> session
                .sessionFixation().none()
            );


        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


}
