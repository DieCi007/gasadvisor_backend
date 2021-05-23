package it.gasadvisor.gas_backend.repository

import it.gasadvisor.gas_backend.model.Privilege
import it.gasadvisor.gas_backend.model.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository : JpaRepository<Role, Int> {
    @Query(value = "select r from Role r left join fetch r.privileges where r.id = :roleId")
    fun getPrivileges(roleId: Int): List<Privilege>
}
