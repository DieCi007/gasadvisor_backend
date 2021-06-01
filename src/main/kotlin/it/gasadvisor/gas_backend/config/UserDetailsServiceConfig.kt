package it.gasadvisor.gas_backend.config

import it.gasadvisor.gas_backend.api.auth.service.AuthenticationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component

@Component
class UserDetailsServiceConfig @Autowired constructor(
    private val service: AuthenticationService
) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {
        val user = service.findByUsernameFetchAuthorities(username.orEmpty())
        return AppUserDetails(user)
    }
}
