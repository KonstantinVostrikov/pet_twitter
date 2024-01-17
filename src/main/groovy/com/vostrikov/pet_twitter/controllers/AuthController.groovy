package com.vostrikov.pet_twitter.controllers

import com.vostrikov.pet_twitter.dto.security.LoginRequest
import com.vostrikov.pet_twitter.services.AuthService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Slf4j
@RestController
@RequestMapping("/auth")
class AuthController {

    @Autowired
    AuthService authService

    @PostMapping("/login")
    def login(@RequestBody @Validated LoginRequest request) {
        try {
            return authService.attemptLogin(request)
        } catch (Exception e) {
            log.error("Security error")
            e.printStackTrace()
        }
    }
}
