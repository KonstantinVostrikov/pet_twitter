package com.vostrikov.pet_twitter.security

import org.springframework.security.authentication.AbstractAuthenticationToken

class UserPrincipalAuthenticationToken extends AbstractAuthenticationToken {

    UserPrincipal principal

    UserPrincipalAuthenticationToken(UserPrincipal principal) {
        super(principal.authorities)
        this.principal = principal
        setAuthenticated(true)
    }

    @Override
    Object getCredentials() {
        return null
    }

    @Override
    UserPrincipal getPrincipal() {
        return principal
    }
}
