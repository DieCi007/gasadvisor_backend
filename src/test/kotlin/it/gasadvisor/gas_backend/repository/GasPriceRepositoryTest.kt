package it.gasadvisor.gas_backend.repository

import it.gasadvisor.gas_backend.model.*
import it.gasadvisor.gas_backend.model.entities.*
import it.gasadvisor.gas_backend.model.enums.CommonFuelType
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
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
        "MI", "MI", 1.1, 1.1, GasStationStatus.ACTIVE
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
    fun `find price should work with municipality`() {
        stationRepository.save(station)
        val otherMunicipalityStation = GasStation(
            2, "diego", "", "", "", "",
            "MI", "TO", 1.1, 1.1, GasStationStatus.ACTIVE
        )
        stationRepository.save(otherMunicipalityStation)
        val priceTO = GasPrice(
            GasPriceId(
                GasStation(2), true, timeRead,
                "desc"
            ), 3.00
        )
        val priceMI = GasPrice(
            GasPriceId(
                GasStation(1),
                false, timeRead, "desc"
            ), 4.00
        )
        priceRepository.saveAll(listOf(priceTO, priceMI))
        explicitFuelRepository.save(ExplicitFuelType(null, "desc", CommonFuelType.GASOLIO))
        val avgMun = priceRepository.findPriceStat(CommonFuelType.GASOLIO, null, "MI")
        assertEquals(3.50, avgMun.getAvg())
        val avgMunProv = priceRepository.findPriceStat(CommonFuelType.GASOLIO, "MI", "MI")
        assertEquals(4.00, avgMunProv.getAvg())
    }

    @Test
    fun `find price should work with province`() {
        stationRepository.save(station)
        val otherProvinceStation = GasStation(
            2, "diego", "", "", "", "",
            "", "TO", 1.1, 1.1, GasStationStatus.ACTIVE
        )
        stationRepository.save(otherProvinceStation)
        val priceTO = GasPrice(
            GasPriceId(
                GasStation(2), true, timeRead,
                "desc"
            ), 3.00
        )
        val priceMI = GasPrice(
            GasPriceId(
                GasStation(1),
                false, timeRead, "desc"
            ), 2.87
        )
        priceRepository.saveAll(listOf(priceOne, priceTO, priceMI))
        explicitFuelRepository.save(ExplicitFuelType(null, "desc", CommonFuelType.GASOLIO))
        val result = priceRepository.findPriceStat(CommonFuelType.GASOLIO, "MI", null)
        assertEquals(2.37, result.getAvg())
        assertEquals(1.87, result.getMin())
        assertEquals(2.87, result.getMax())
    }

    @Test
    fun `find price stat should find correct prices`() {
        stationRepository.save(station)
        explicitFuelRepository.save(ExplicitFuelType(null, "desc", CommonFuelType.GASOLIO))
        val newerDate = timeRead.plus(1, ChronoUnit.DAYS)
        val priceNewerOne = GasPrice(
            GasPriceId(
                GasStation(1), true, newerDate,
                "desc"
            ), 2.00
        )
        val priceNewerTwo = GasPrice(
            GasPriceId(
                GasStation(1), false, newerDate,
                "desc"
            ), 4.00
        )
        priceRepository.saveAll(listOf(priceOne, priceNewerOne, priceNewerTwo))
        val avgPrice = priceRepository.findPriceStat(CommonFuelType.GASOLIO, null, null)
        assertEquals(3.00, avgPrice.getAvg())
        assertEquals(2.00, avgPrice.getMin())
        assertEquals(4.00, avgPrice.getMax())
    }

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
