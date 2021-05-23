package it.gasadvisor.gas_backend.config

import it.gasadvisor.gas_backend.api.auth.service.AuthenticationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class UserDetailsServiceConfig @Autowired constructor(
    private val service: AuthenticationService
) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {
        val user = service.findByUsername(username.orEmpty())
        val roleId = user.role.id
        println("user role id is $roleId")
        val privileges = service.getUserPrivilegesByRoleId(roleId)
        println(privileges)
        return UserDetailsConfig(user)
    }
}
