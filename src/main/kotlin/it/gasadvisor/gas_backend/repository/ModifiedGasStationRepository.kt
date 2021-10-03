package it.gasadvisor.gas_backend.repository

import it.gasadvisor.gas_backend.model.entities.ModifiedGasStation
import org.springframework.data.jpa.repository.JpaRepository

interface ModifiedGasStationRepository : JpaRepository<ModifiedGasStation, Long>
