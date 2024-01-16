package com.vostrikov.pet_twitter.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit

@Component
class JwtIssuer {

    @Autowired
    JwtProperties jwtProperties

    String issue(String userId, String email, List<String> roles) {

        return JWT.create()
                .withSubject(userId)
                .withExpiresAt(Instant.now() + Duration.of(1, ChronoUnit.HOURS))
                .withClaim("email", email)
                .withClaim("auth", roles)
                .sign(Algorithm.HMAC256(jwtProperties.secretKey))
    }

}
