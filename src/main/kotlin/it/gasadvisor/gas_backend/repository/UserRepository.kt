package it.gasadvisor.gas_backend.repository

import it.gasadvisor.gas_backend.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): Optional<User>

    @Query("select u from User u left join fetch u.role r join fetch r.privileges where u.username = :username")
    fun findByUsernameFetchAuthorities(@Param("username") username: String): Optional<User>

}
