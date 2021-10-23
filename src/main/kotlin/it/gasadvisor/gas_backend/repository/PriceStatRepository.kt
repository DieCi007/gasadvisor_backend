package it.gasadvisor.gas_backend.repository

import it.gasadvisor.gas_backend.model.entities.PriceStat
import it.gasadvisor.gas_backend.model.enums.CommonFuelType
import it.gasadvisor.gas_backend.model.enums.PriceStatType
import it.gasadvisor.gas_backend.repository.contract.IDatePrice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
interface PriceStatRepository : JpaRepository<PriceStat, Long> {

    @Query(
        "select ps.price as price, ps.gasStat.date as date from PriceStat ps " +
                "where ps.priceStatType = :statType and ps.gasStat.date >= :dateFrom " +
                "and ps.fuelType = :fuelType order by ps.gasStat.date"
    )
    fun findPriceTrend(
        @Param("dateFrom") dateFrom: Instant,
        @Param("fuelType") fuelType: CommonFuelType,
        @Param("statType") statType: PriceStatType
    ): List<IDatePrice>
}
