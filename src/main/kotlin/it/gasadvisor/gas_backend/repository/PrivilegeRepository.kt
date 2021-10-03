package it.gasadvisor.gas_backend.repository

import it.gasadvisor.gas_backend.model.entities.Privilege
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PrivilegeRepository : JpaRepository<Privilege, String> {

}
