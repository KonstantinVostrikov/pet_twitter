package com.vostrikov.pet_twitter.security

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties("security.jwt")
class JwtProperties {
    String secretKey
}
