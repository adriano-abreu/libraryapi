package com.adrianodeabreu.libraryapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .formLogin(configurer -> configurer.loginPage("/login").permitAll())
                .authorizeHttpRequests(authorize -> {
                            authorize.requestMatchers("/autores/**").hasRole("ADMIN");
                            authorize.requestMatchers("/livros/**").hasAnyRole("USER", "ADMIN");
                            authorize.requestMatchers("/login").permitAll();
                            authorize.anyRequest().permitAll();
                    }
                )
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {


        UserDetails user1 = User.withUsername("user")
                .password(passwordEncoder.encode("123456"))
                .roles("USER")
                .build();

        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder.encode("654321"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager( user1, admin);
    }
}
