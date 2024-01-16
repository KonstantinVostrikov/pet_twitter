package com.vostrikov.pet_twitter.controllers

import com.vostrikov.pet_twitter.dto.security.LoginRequest
import com.vostrikov.pet_twitter.dto.security.LoginResponse
import com.vostrikov.pet_twitter.security.JwtIssuer
import com.vostrikov.pet_twitter.security.UserPrincipal
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
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

    @Autowired
    AuthenticationManager authenticationManager

    @PostMapping("/login")
    def login(@RequestBody @Validated LoginRequest request) {
        def authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email, request.password)
        )
        SecurityContextHolder.context.setAuthentication(authentication)
        def principal = authentication.principal as UserPrincipal
        def roles = principal.authorities.stream().map(GrantedAuthority::getAuthority).toList()

        def token = jwtIssuer.issue(principal.userId, principal.email, roles)
        return LoginResponse.builder()
                .accessToken(token)
                .build()
    }
}
