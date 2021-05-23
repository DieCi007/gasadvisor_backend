package it.gasadvisor.gas_backend.api.auth.service

import it.gasadvisor.gas_backend.exception.NotFoundException
import it.gasadvisor.gas_backend.exception.UserNotFoundException
import it.gasadvisor.gas_backend.model.*
import it.gasadvisor.gas_backend.repository.PrivilegeRepository
import it.gasadvisor.gas_backend.repository.RoleRepository
import it.gasadvisor.gas_backend.repository.UserRepository
import org.hibernate.Hibernate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.annotation.PostConstruct

@Service
class AuthenticationService @Autowired constructor(
    private val repository: UserRepository,
    private val roleRepository: RoleRepository,
    private val privilegeRepository: PrivilegeRepository
) {

    fun findByUsername(username: String): User {
        return repository.findByUsername(username)
            .orElseThrow { throw UserNotFoundException("User $username not found") }
    }

    fun getUserPrivilegesByRoleId(roleId: Int): List<Privilege> {
        return roleRepository.getPrivileges(roleId)
    }

    @PostConstruct
    fun firstUser() {
//        val a = privilegeRepository.save(Privilege(name = PrivilegeName.READ_ALL, description = "read all application data"))
//        val b = privilegeRepository.save(Privilege(name = PrivilegeName.WRITE_ALL, description = "write all application data"))
//        val c = roleRepository.save(Role(name = RoleName.ADMIN, privileges = setOf(a, b)))
//        roleRepository.save(Role(name = RoleName.END_USER))
//        roleRepository.save(Role(name = RoleName.GUEST))
//        repository.findByUsername("admin").orElseGet {
//            repository.save(
//                User(
//                    username = "admin",
//                    password = "\$2y\$10\$WZk8io6VFSEe1CzBkl3V2OyxD7n.BRmnoBvCh6efSw8LhTXcDpNkK",
//                    role = c
//                )
//            )
//        }
    }

}
