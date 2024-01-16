package com.vostrikov.pet_twitter.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class WebSecurityConfig {

    @Autowired
    CustomUserDetailService customUserDetailService

    @Bean
    SecurityFilterChain applicationSecurity(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) {
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

        http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement { s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
                .formLogin(AbstractHttpConfigurer::disable)
                .securityMatcher("/**")
                .authorizeHttpRequests(registry -> registry
//                        .requestMatchers("/**").permitAll()
                        .requestMatchers("/users/create").permitAll()
                        .requestMatchers("/auth/login").permitAll()
                        .anyRequest().authenticated()
                )


        return http.build()
    }



    @Bean
    AuthenticationManager authenticationManager(HttpSecurity http, @Qualifier("passwordEncoder") PasswordEncoder passwordEncoder) {
        def builder = http.getSharedObject(AuthenticationManagerBuilder.class)
        builder
                .userDetailsService(customUserDetailService)
                .passwordEncoder(passwordEncoder)
        return builder.build()
    }

}



