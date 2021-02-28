package it.gasadvisor.gas_backend.api.auth.service

import it.gasadvisor.gas_backend.api.auth.domain.UserRole
import it.gasadvisor.gas_backend.exception.UserNotFoundException
import it.gasadvisor.gas_backend.model.User
import it.gasadvisor.gas_backend.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

@Service
class AuthenticationService @Autowired constructor(
    private val repository: UserRepository
) {

    fun findByUsername(username: String): User {
        return repository.findByUsername(username)
            .orElseThrow { throw UserNotFoundException("User $username not found") }
    }


    @PostConstruct
    private fun firstUser() {
        repository.findByUsername("admin").orElseGet {
            repository.save(User(username = "admin", password = "\$2y\$10\$WZk8io6VFSEe1CzBkl3V2OyxD7n.BRmnoBvCh6efSw8LhTXcDpNkK", role = UserRole.ADMIN))
        }
    }
}
