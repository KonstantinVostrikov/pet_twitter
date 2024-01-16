package com.vostrikov.pet_twitter.security

import com.auth0.jwt.interfaces.DecodedJWT
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component

@Component
class JwtToPrincipalConverter {

    UserPrincipal convert(DecodedJWT jwt) {

        def userId = jwt.subject
        def email = jwt.getClaim("email").asString()
        def authorities = extractAuthoritiesFromClaim(jwt)

        return new UserPrincipal(userId: userId, email: email, authorities: authorities)
    }


    private List<SimpleGrantedAuthority> extractAuthoritiesFromClaim(DecodedJWT jwt) {
        def claim = jwt.getClaim("auth")
        if (claim.isNull() || claim.isMissing()) return []
        return claim.asList(SimpleGrantedAuthority.class)
    }
}
