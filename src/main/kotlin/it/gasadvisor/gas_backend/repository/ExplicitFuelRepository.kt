package it.gasadvisor.gas_backend.repository

import it.gasadvisor.gas_backend.api.gas.explicit_fuel.contract.IExplicitFuelType
import it.gasadvisor.gas_backend.model.entities.ExplicitFuelType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ExplicitFuelRepository : JpaRepository<ExplicitFuelType, Long> {
    @Query("select f.id as id, f.name as name, f.commonType as type from ExplicitFuelType f")
    fun adminGetAll(): List<IExplicitFuelType>
}
