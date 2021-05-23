package it.gasadvisor.gas_backend.api.auth.service

import it.gasadvisor.gas_backend.exception.UserNotFoundException
import it.gasadvisor.gas_backend.model.Role
import it.gasadvisor.gas_backend.model.RoleName
import it.gasadvisor.gas_backend.model.User
import it.gasadvisor.gas_backend.repository.PrivilegeRepository
import it.gasadvisor.gas_backend.repository.RoleRepository
import it.gasadvisor.gas_backend.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

@Service
class AuthenticationService @Autowired constructor(
    private val repository: UserRepository,
    private val roleRepository: RoleRepository,
    private val privilegeRepository: PrivilegeRepository
) {

    fun findByUsernameFetchAuthorities(username: String): User {
        return repository.findByUsername(username)
            .orElseThrow { throw UserNotFoundException("User $username not found") }
    }

    @PostConstruct
    fun firstUser() {
//        val a = privilegeRepository.save(Privilege(name = PrivilegeName.READ_ALL, description = "read all application data"))
//        val b = privilegeRepository.save(Privilege(name = PrivilegeName.WRITE_ALL, description = "write all application data"))
//        val c = roleRepository.save(Role(name = RoleName.ADMIN, privileges = setOf(a, b)))
//        roleRepository.save(Role(name = RoleName.END_USER))
//        roleRepository.save(Role(name = RoleName.GUEST))
        val role = Role(null, RoleName.ADMIN, emptySet(), emptySet())
        role.id = 1
        repository.findByUsername("admin").orElseGet {
            repository.save(
                User(
                    username = "admin",
                    password = BCryptPasswordEncoder(10).encode("admin"),
                    role = role
                )
            )
        }
    }

}
