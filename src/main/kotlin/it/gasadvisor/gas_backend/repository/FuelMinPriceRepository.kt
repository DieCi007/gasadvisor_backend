package it.gasadvisor.gas_backend.repository

import it.gasadvisor.gas_backend.model.entities.FuelMinPrice
import it.gasadvisor.gas_backend.model.enums.CommonFuelType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface FuelMinPriceRepository : JpaRepository<FuelMinPrice, Long> {
    fun findByType(type: CommonFuelType): Optional<FuelMinPrice>
}
