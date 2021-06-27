package it.gasadvisor.gas_backend.api.auth.service

import it.gasadvisor.gas_backend.exception.UserNotFoundException
import it.gasadvisor.gas_backend.model.*
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
    private val privilegeRepository: PrivilegeRepository,
    private val roleRepository: RoleRepository
) {

    fun findByUsernameFetchAuthorities(username: String): User {
        return repository.findByUsernameFetchAuthorities(username)
            .orElseThrow { throw UserNotFoundException("User $username not found") }
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
//                username = "admin",
//                password = BCryptPasswordEncoder(10).encode("admin"),
//                role = Role(RoleName.ADMIN, emptySet())
//            )
//        )
//    }
}
