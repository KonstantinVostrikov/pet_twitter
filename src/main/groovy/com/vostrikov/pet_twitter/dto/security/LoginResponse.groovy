package com.vostrikov.pet_twitter.dto.security

import groovy.transform.builder.Builder

@Builder
class LoginResponse {
    private String accessToken

    String getAccessToken() {
        return accessToken
    }

    void setAccessToken(String accessToken) {
        this.accessToken = accessToken
    }
}
