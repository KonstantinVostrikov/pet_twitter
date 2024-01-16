package com.vostrikov.pet_twitter.controllers

import com.vostrikov.pet_twitter.dto.security.LoginRequest
import com.vostrikov.pet_twitter.dto.security.LoginResponse
import com.vostrikov.pet_twitter.security.JwtIssuer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController {

    @Autowired
    private JwtIssuer jwtIssuer

    @PostMapping("/login")
    def login(@RequestBody @Validated LoginRequest request) {
        def token = jwtIssuer.issue("userId", request.email, ["USER"])
        return LoginResponse.builder()
                .accessToken(token)
                .build()
    }
}
