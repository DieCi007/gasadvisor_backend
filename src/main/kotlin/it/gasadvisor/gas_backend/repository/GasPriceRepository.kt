package it.gasadvisor.gas_backend.repository

import it.gasadvisor.gas_backend.api.gas.station.contract.GetStationPriceResponse
import it.gasadvisor.gas_backend.model.entities.GasPrice
import it.gasadvisor.gas_backend.model.entities.GasPriceId
import it.gasadvisor.gas_backend.model.enums.CommonFuelType
import it.gasadvisor.gas_backend.repository.contract.IPriceStat
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface GasPriceRepository : JpaRepository<GasPrice, GasPriceId>, JpaSpecificationExecutor<GasPrice> {
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

    @Query(
        "select avg(gp.price) as avg, min(gp.price) as min, max(gp.price) as max from GasPrice gp where gp.id.description in " +
                "(select ft.name from ExplicitFuelType ft where ft.commonType = :fuelType) and gp.id.readDate = " +
                "(select max(gp1.id.readDate) from GasPrice gp1 where gp1.id.gasStation.id = gp.id.gasStation.id) " +
                "and gp.id.gasStation.province = coalesce(:province, gp.id.gasStation.province) " +
                "and gp.id.gasStation.municipality = coalesce(:municipality, gp.id.gasStation.municipality)"
    )
    fun findPriceStat(
        @Param("fuelType") fuelType: CommonFuelType,
        @Param("province") province: String?,
        @Param("municipality") municipality: String?
    ): IPriceStat

}
