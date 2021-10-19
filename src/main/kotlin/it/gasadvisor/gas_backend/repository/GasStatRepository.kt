package it.gasadvisor.gas_backend.repository

import it.gasadvisor.gas_backend.model.entities.GasStat
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface GasStatRepository : JpaRepository<GasStat, Long> {

}
