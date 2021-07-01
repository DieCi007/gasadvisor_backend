package it.gasadvisor.gas_backend.repository

import it.gasadvisor.gas_backend.api.gas.station.contract.GetStationPriceResponse
import it.gasadvisor.gas_backend.model.GasPrice
import it.gasadvisor.gas_backend.model.GasPriceId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface GasPriceRepository : JpaRepository<GasPrice, GasPriceId> {
    @Query(
        "select new it.gasadvisor.gas_backend.api.gas.station.contract.GetStationPriceResponse(p.price, p.id.isSelf, p.id.readDate, p.id.description) " +
                "from GasPrice p where p.id.gasStation.id = :id and " +
                "p.id.readDate = (select max(g.id.readDate) from GasPrice g where g.id.gasStation.id = :id)"
    )
    fun findLatestPriceByStationId(@Param("id") id: Long): List<GetStationPriceResponse>

    @Query(
        "select distinct gp.id.description from GasPrice gp where gp.id.description " +
                "not in (select eft.name from ExplicitFuelType eft)"
    )
    fun findNotSavedFuelTypes(): List<String>
}
