package it.gasadvisor.gas_backend.repository

import it.gasadvisor.gas_backend.model.entities.ModifiedGasStation
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ModifiedGasStationRepository : JpaRepository<ModifiedGasStation, Long> {
    fun findByStationId(id: Long): Optional<ModifiedGasStation>
}
