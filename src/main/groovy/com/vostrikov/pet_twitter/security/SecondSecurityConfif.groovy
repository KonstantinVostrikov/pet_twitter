package com.vostrikov.pet_twitter.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class SecondSecurityConfif {

    @Bean("passwordEncoder")
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder()
    }
}
