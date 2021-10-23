package it.gasadvisor.gas_backend.repository

import it.gasadvisor.gas_backend.model.entities.GasStat
import it.gasadvisor.gas_backend.model.entities.PriceStat
import it.gasadvisor.gas_backend.model.enums.CommonFuelType
import it.gasadvisor.gas_backend.model.enums.PriceStatType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.Instant
import java.time.temporal.ChronoUnit

@JpaTest
class PriceStatRepositoryTest @Autowired constructor(
    private val repo: PriceStatRepository,
    private val gasStatRepo: GasStatRepository
) {
    private val today: Instant = Instant.now()
    private val yesterday: Instant = today.minus(1, ChronoUnit.DAYS)
    private val tomorrow: Instant = today.plus(1, ChronoUnit.DAYS)

    @Test
    fun `should find correct price trend`() {
        val statToday = gasStatRepo.save(GasStat(today))
        val statTomorrow = gasStatRepo.save(GasStat(tomorrow))
        val statYesterday = gasStatRepo.save(GasStat(yesterday))

        repo.saveAll(
            listOf(
                PriceStat(CommonFuelType.BENZINA, 1.0, PriceStatType.MIN, statToday),
                PriceStat(CommonFuelType.BENZINA, 2.0, PriceStatType.AVG, statToday),
                PriceStat(CommonFuelType.BENZINA, 3.0, PriceStatType.MAX, statToday),
                PriceStat(CommonFuelType.BENZINA, 2.0, PriceStatType.MIN, statYesterday),
                PriceStat(CommonFuelType.BENZINA, 3.0, PriceStatType.AVG, statYesterday),
                PriceStat(CommonFuelType.BENZINA, 4.0, PriceStatType.MAX, statYesterday),
                PriceStat(CommonFuelType.BENZINA, 3.0, PriceStatType.MIN, statTomorrow),
                PriceStat(CommonFuelType.BENZINA, 4.0, PriceStatType.AVG, statTomorrow),
                PriceStat(CommonFuelType.BENZINA, 5.0, PriceStatType.MAX, statTomorrow),

                PriceStat(CommonFuelType.GASOLIO, 4.0, PriceStatType.MIN, statToday),
                PriceStat(CommonFuelType.GASOLIO, 5.0, PriceStatType.AVG, statToday),
                PriceStat(CommonFuelType.GASOLIO, 6.0, PriceStatType.MAX, statToday),
                PriceStat(CommonFuelType.GASOLIO, 5.0, PriceStatType.MIN, statYesterday),
                PriceStat(CommonFuelType.GASOLIO, 6.0, PriceStatType.AVG, statYesterday),
                PriceStat(CommonFuelType.GASOLIO, 7.0, PriceStatType.MAX, statYesterday),
                PriceStat(CommonFuelType.GASOLIO, 6.0, PriceStatType.MIN, statTomorrow),
                PriceStat(CommonFuelType.GASOLIO, 7.0, PriceStatType.AVG, statTomorrow),
                PriceStat(CommonFuelType.GASOLIO, 8.0, PriceStatType.MAX, statTomorrow),
            )
        )
        val minTrendBenzina = repo.findPriceTrend(
            yesterday.minus(1, ChronoUnit.DAYS),
            CommonFuelType.BENZINA, PriceStatType.MIN
        )
        val avgTrendBenzina = repo.findPriceTrend(
            yesterday.minus(1, ChronoUnit.DAYS),
            CommonFuelType.BENZINA, PriceStatType.AVG
        )
        val maxTrendBenzina = repo.findPriceTrend(
            yesterday.truncatedTo(ChronoUnit.SECONDS), CommonFuelType.BENZINA, PriceStatType.MAX
        )

        assertEquals(3, minTrendBenzina.size)
        assertEquals(3, avgTrendBenzina.size)
        assertEquals(3, maxTrendBenzina.size)
        assertEquals(2.0, minTrendBenzina[0].getPrice())
        assertEquals(2.0, avgTrendBenzina[1].getPrice())
        assertEquals(5.0, maxTrendBenzina[2].getPrice())

        val minTrendGasolio = repo.findPriceTrend(
            yesterday.minus(1, ChronoUnit.DAYS),
            CommonFuelType.GASOLIO, PriceStatType.MIN
        )
        val avgTrendGasolio = repo.findPriceTrend(
            yesterday.minus(1, ChronoUnit.DAYS),
            CommonFuelType.GASOLIO, PriceStatType.AVG
        )
        val maxTrendGasolio = repo.findPriceTrend(
            yesterday.truncatedTo(ChronoUnit.SECONDS), CommonFuelType.GASOLIO, PriceStatType.MAX
        )
        assertEquals(3, minTrendGasolio.size)
        assertEquals(3, avgTrendGasolio.size)
        assertEquals(3, maxTrendGasolio.size)
        assertEquals(5.0, minTrendGasolio[0].getPrice())
        assertEquals(5.0, avgTrendGasolio[1].getPrice())
        assertEquals(8.0, maxTrendGasolio[2].getPrice())

    }

    @BeforeEach
    fun reset() {
        repo.deleteAllInBatch()
        gasStatRepo.deleteAllInBatch()
    }
}
