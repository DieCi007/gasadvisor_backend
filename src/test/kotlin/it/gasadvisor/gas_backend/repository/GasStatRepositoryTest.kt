package it.gasadvisor.gas_backend.repository

import it.gasadvisor.gas_backend.model.entities.GasStat
import it.gasadvisor.gas_backend.model.entities.PriceStat
import it.gasadvisor.gas_backend.model.enums.CommonFuelType
import it.gasadvisor.gas_backend.model.enums.PriceStatType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import java.time.Instant
import java.time.temporal.ChronoUnit

@JpaTest
class GasStatRepositoryTest @Autowired constructor(
    val repository: GasStatRepository,
    val priceStatRepository: PriceStatRepository
) {
    @Test
    fun `should also save all price stats`() {
        val priceStat = PriceStat(CommonFuelType.GASOLIO, 1.00, PriceStatType.AVG)
        val gasStat = GasStat(
            null, Instant.now(),
            null, null, null, null, listOf(priceStat)
        )

        assertDoesNotThrow { repository.save(gasStat) }
    }

    @Test
    fun `should find by date`() {
        val gasStat = repository.save(GasStat(Instant.now().truncatedTo(ChronoUnit.DAYS)))
        priceStatRepository.saveAll(
            listOf(
                PriceStat(CommonFuelType.GASOLIO, 1.00, PriceStatType.MIN, gasStat),
                PriceStat(CommonFuelType.GASOLIO, 2.00, PriceStatType.AVG, gasStat),
                PriceStat(CommonFuelType.GASOLIO, 3.00, PriceStatType.MAX, gasStat)
            )
        )
        val res = repository.findByDate(Instant.now().truncatedTo(ChronoUnit.DAYS))
        assertTrue(res.isPresent)
        val prices = repository.findPriceStatsByDate(Instant.now().truncatedTo(ChronoUnit.DAYS))
        assertEquals(3, prices.size)
    }

    @BeforeEach
    fun clear() {
        priceStatRepository.deleteAllInBatch()
        repository.deleteAllInBatch()
    }
}
