package com.example.taskmanager.config;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final SuccessHandler successHandler;
    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    @SneakyThrows
    SecurityFilterChain securityFilterChain(HttpSecurity http){
        return http.authorizeHttpRequests(request -> {
            request
                    .requestMatchers("/registration", "/authentication").permitAll()
                    .requestMatchers("/home/**", "/calendar/**").authenticated();

            // Разрешить доступ ко всем остальным URL
            request.anyRequest().permitAll();
        }).formLogin(form -> {
            form.loginPage("/authentication");
            form.successHandler(successHandler);
        }).csrf(AbstractHttpConfigurer::disable).build();
    }


}
