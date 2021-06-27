package it.gasadvisor.gas_backend.repository

import it.gasadvisor.gas_backend.model.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.TestPropertySource

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("classpath:application-test.properties")
class UserRepositoryTest @Autowired constructor(
    val userRepo: UserRepository,
    val roleRepo: RoleRepository,
    val privilegeRepo: PrivilegeRepository
) {
    private val privilege = Privilege(name = PrivilegeName.WRITE_ALL, description = "description")
    private val role = Role(name = RoleName.GUEST, privileges = setOf(privilege))
    private val user = User( null,"user", "pass", role)

    @BeforeEach
    fun addDB() {
        privilegeRepo.save(privilege)
        roleRepo.save(role)
        userRepo.save(user)
    }

    @AfterEach
    fun removeUser() {
        userRepo.deleteAll()
        userRepo.flush()
    }

    @Test
    fun `should find by username`() {
        val userFound = userRepo.findByUsername("user")
        assertThat(userFound.get().username).isEqualTo(user.username)
    }

    @Test
    fun `should find by username and get authorities`() {
        val userFound = userRepo.findByUsernameFetchAuthorities("user")
        assertThat(userFound.get().role.privileges).anyMatch { e -> e.name == privilege.name }
    }

}
