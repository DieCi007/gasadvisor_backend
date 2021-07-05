package it.gasadvisor.gas_backend.repository

import it.gasadvisor.gas_backend.model.CommonFuelType
import it.gasadvisor.gas_backend.model.GasStat
import it.gasadvisor.gas_backend.model.PriceStat
import it.gasadvisor.gas_backend.model.PriceStatType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import java.time.Instant

@JpaTest
class GasStatRepositoryTest @Autowired constructor(
    val repository: GasStatRepository
) {
    @Test
    fun `should also save all price stats`() {
        val priceStat = PriceStat(CommonFuelType.GASOLIO, 1.00, PriceStatType.AVG)
        val gasStat = GasStat(
            null, Instant.now(), listOf(priceStat),
            null, null, null, null
        )
        assertDoesNotThrow { repository.save(gasStat) }
    }

    @BeforeEach
    fun clear() {
        repository.deleteAllInBatch()
    }
}
