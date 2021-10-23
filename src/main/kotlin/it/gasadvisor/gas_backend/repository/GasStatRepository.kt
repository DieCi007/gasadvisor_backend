package it.gasadvisor.gas_backend.repository

import it.gasadvisor.gas_backend.model.entities.GasStat
import it.gasadvisor.gas_backend.model.entities.PriceStat
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.*

@Repository
interface GasStatRepository : JpaRepository<GasStat, Long> {

    fun findByDate(date: Instant): Optional<GasStat>

    @Query("select gs.prices from GasStat gs where gs.date = :date")
    fun findPriceStatsByDate(@Param("date") date: Instant): List<PriceStat>
}
