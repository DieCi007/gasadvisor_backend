package it.gasadvisor.gas_backend.repository

import it.gasadvisor.gas_backend.model.*
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.TestPropertySource
import java.time.Instant
import java.time.temporal.ChronoUnit

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GasPriceRepositoryTest @Autowired constructor(
    val priceRepository: GasPriceRepository,
    val stationRepository: GasStationRepository,
    val explicitFuelRepository: ExplicitFuelRepository
) {
    private val station = GasStation(
        1, "diego", "", "", "", "",
        "", "", 1.1, 1.1,
    )
    private val timeRead: Instant = Instant.now()
    private val priceOne = GasPrice(
        GasPriceId(
            GasStation(1), true, timeRead,
            "desc"
        ), 1.87
    )

    private val priceTwo = GasPrice(
        GasPriceId(
            GasStation(1), true, timeRead,
            "desc"
        ), 2.87
    )

    @Test
    fun `should get not saved fuel types`() {
        explicitFuelRepository.save(ExplicitFuelType(null, "gasolio", CommonFuelType.GASOLIO))
        stationRepository.save(station)
        val metano = GasPrice(GasPriceId(station, true, timeRead, "metano"), 1.00)
        val gasolio = GasPrice(GasPriceId(station, true, timeRead, "gasolio"), 1.01)
        val benzina = GasPrice(GasPriceId(station, true, timeRead, "benzina"), 1.03)
        val benzinaDouble = GasPrice(GasPriceId(station, false, timeRead, "benzina"), 1.04)
        priceRepository.saveAll(listOf(metano, gasolio, benzina, benzinaDouble))
        val result = priceRepository.findNotSavedFuelTypes()
        assertEquals(result.size, 2)
    }

    @Test
    fun `save should update station`() {
        stationRepository.save(station)
        priceRepository.save(priceOne)
        val second = priceRepository.save(priceTwo)
        val fromDb = priceRepository.findById(
            GasPriceId(
                GasStation(1),
                true, timeRead, "desc"
            )
        ).get()
        assertEquals(second.price, fromDb.price)
    }

    @Test
    fun `should find last price for station`() {
        stationRepository.save(station)
        priceRepository.save(priceOne)
        val recentDate = timeRead.plus(1, ChronoUnit.DAYS)
        val moreRecentPriceOne = GasPrice(
            GasPriceId(
                GasStation(1), true, recentDate,
                "desc"
            ), 5.87
        )
        val moreRecentPriceTwo = GasPrice(
            GasPriceId(
                GasStation(1), true, recentDate,
                "desc1"
            ), 5.87
        )
        priceRepository.save(moreRecentPriceOne)
        priceRepository.save(moreRecentPriceTwo)
        val res = priceRepository.findLatestPriceByStationId(station.id)
        assertTrue(res.isNotEmpty())
        assertTrue(res.stream().allMatch { p -> p.price == 5.87 })
    }

    @BeforeEach
    fun clear() {
        explicitFuelRepository.deleteAllInBatch()
        priceRepository.deleteAllInBatch()
        stationRepository.deleteAllInBatch()
    }
}
