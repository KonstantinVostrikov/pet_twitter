package com.vostrikov.pet_twitter.services.impl

import com.vostrikov.pet_twitter.dto.security.LoginRequest
import com.vostrikov.pet_twitter.dto.security.LoginResponse
import com.vostrikov.pet_twitter.security.JwtIssuer
import com.vostrikov.pet_twitter.security.UserPrincipal
import com.vostrikov.pet_twitter.services.AuthService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class AuthServiceImpl implements AuthService {


    @Autowired
    private JwtIssuer jwtIssuer
    @Autowired
    AuthenticationManager authenticationManager

    @Override
    LoginResponse attemptLogin(LoginRequest request) {
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
