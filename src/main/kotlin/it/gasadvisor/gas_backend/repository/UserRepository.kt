package it.gasadvisor.gas_backend.repository

import it.gasadvisor.gas_backend.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, String> {
    fun findByUsername(username: String): Optional<User>

    @Query("select u from User u left join fetch u.role r join fetch r.privileges where u.username = :username")
    fun findByUsernameFetchAuthorities(@Param("username") username: String): Optional<User>

}
