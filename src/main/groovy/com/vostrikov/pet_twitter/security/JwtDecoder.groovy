package com.vostrikov.pet_twitter.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class JwtDecoder {
    @Autowired
    JwtProperties properties

    DecodedJWT decode(String token) {
        JWT.require(Algorithm.HMAC256(properties.secretKey))
        .build()
        .verify(token)
    }
}
