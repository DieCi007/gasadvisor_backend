package it.gasadvisor.gas_backend.api.auth.service

import it.gasadvisor.gas_backend.api.auth.contract.RefreshTokenRequest
import it.gasadvisor.gas_backend.api.auth.contract.RefreshTokenResponse
import it.gasadvisor.gas_backend.api.auth.contract.UserMeResponse
import it.gasadvisor.gas_backend.exception.NotFoundException
import it.gasadvisor.gas_backend.exception.UserNotFoundException
import it.gasadvisor.gas_backend.model.User
import it.gasadvisor.gas_backend.repository.PrivilegeRepository
import it.gasadvisor.gas_backend.repository.RoleRepository
import it.gasadvisor.gas_backend.repository.UserRepository
import it.gasadvisor.gas_backend.util.JwtHelper
import it.gasadvisor.gas_backend.util.SecurityUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Service

@Service
class AuthenticationService @Autowired constructor(
    private val repository: UserRepository,
    private val jwtHelper: JwtHelper,
    private val privilegeRepository: PrivilegeRepository,
    private val roleRepository: RoleRepository
) {

    fun findByUsernameFetchAuthorities(username: String): User {
        return repository.findByUsernameFetchAuthorities(username)
            .orElseThrow { throw UserNotFoundException("User $username not found") }
    }

    fun refreshToken(request: RefreshTokenRequest): RefreshTokenResponse {
        val claims = jwtHelper.readJwt(request.token)
        val username = claims.subject
        val jwtAuthorities = claims[JwtHelper.AUTHORITIES_CLAIM] as List<*>
        val authorities = jwtAuthorities.map { m -> SimpleGrantedAuthority(m as String) }.toSet()
        val authToken = jwtHelper.createPrimaryJwt(username, authorities)
        val refreshToken = jwtHelper.createRefreshJwt(username, authorities)
        return RefreshTokenResponse(authToken, refreshToken)
    }

    fun getMe(): UserMeResponse {
        return repository.getUserMe(SecurityUtils.getAuthenticatedUsername())
            .orElseThrow { NotFoundException("User not found") }
    }

//    @PostConstruct
//    fun firstUser() {
//        privilegeRepository.save(Privilege(name = PrivilegeName.READ_ALL, description = "read all application data"))
//        privilegeRepository.save(Privilege(name = PrivilegeName.WRITE_ALL, description = "write all application data"))
//        roleRepository.save(
//            Role(
//                name = RoleName.ADMIN, privileges = setOf(
//                    Privilege(PrivilegeName.WRITE_ALL, null), Privilege(PrivilegeName.READ_ALL, null)
//                )
//            )
//        )
//        roleRepository.save(Role(name = RoleName.END_USER, emptySet()))
//        roleRepository.save(Role(name = RoleName.GUEST, emptySet()))
//        repository.save(
//            User(
//                id = null,
//                username = "admin",
//                password = BCryptPasswordEncoder(10).encode("admin"),
//                role = Role(RoleName.ADMIN, emptySet())
//            )
//        )
//    }
}
