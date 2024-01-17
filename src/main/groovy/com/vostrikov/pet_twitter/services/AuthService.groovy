package com.vostrikov.pet_twitter.services

import com.vostrikov.pet_twitter.dto.security.LoginRequest
import com.vostrikov.pet_twitter.dto.security.LoginResponse

interface AuthService {
    LoginResponse attemptLogin(LoginRequest request)
}