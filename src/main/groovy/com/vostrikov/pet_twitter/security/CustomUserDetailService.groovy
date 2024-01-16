package com.vostrikov.pet_twitter.security

import com.vostrikov.pet_twitter.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component

@Component
class CustomUserDetailService implements UserDetailsService {

    @Autowired
    UserService userService

    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        def user = userService.findByEmail(username)
        return new UserPrincipal(userId: user.id, email: user.email, authorities: [new SimpleGrantedAuthority(user.role)], password: user.password)
    }
}
