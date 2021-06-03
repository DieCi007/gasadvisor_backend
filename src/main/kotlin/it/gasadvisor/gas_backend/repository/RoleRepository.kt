package it.gasadvisor.gas_backend.repository

import it.gasadvisor.gas_backend.model.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository : JpaRepository<Role, String>
