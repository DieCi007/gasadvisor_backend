package it.gasadvisor.gas_backend.repository

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
import kotlin.math.log

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
        ), 1.8
    )

    private val priceTwo = GasPrice(
        GasPriceId(
            GasStation(1), true, timeRead,
            "desc"
        ), 2.87
    )

    @Test
    fun `should find medium price`() {
        explicitFuelRepository.save(ExplicitFuelType(null, "desc", CommonFuelType.GASOLIO))
        val stationTwo = GasStation(
            2, "diego", "", "", "", "",
            "MI", "LE", 1.1, 1.1, GasStationStatus.ACTIVE
        )
        stationRepository.saveAll(listOf(station, stationTwo))
        val pastOneMonthDate = timeRead.minus(31, ChronoUnit.DAYS)
        val newerDate = timeRead.plus(2, ChronoUnit.DAYS)
        val priceOld = GasPrice(2.00, 1, pastOneMonthDate, "desc")
        val priceNewerTwo = GasPrice(4.00, 1, newerDate, "desc")
        val priceOldProvince = GasPrice(1.83, 2, pastOneMonthDate, "desc")
        val priceNewProvince = GasPrice(3.0, 2, timeRead, "desc")
        val priceNewerProvince = GasPrice(1.0, 2, newerDate, "desc")
        priceRepository.saveAll(
            listOf(
                priceOne, priceOld, priceNewerTwo,
                priceOldProvince, priceNewProvince, priceNewerProvince
            )
        )
        val avgPrice = priceRepository.findMediumPrice(
            CommonFuelType.GASOLIO, null,
            timeRead.minus(30, ChronoUnit.DAYS)
        )
        val avgPriceProvince = priceRepository.findMediumPrice(
            CommonFuelType.GASOLIO, "LE",
            timeRead.minus(30, ChronoUnit.DAYS)
        )
        assertEquals(2.45, avgPrice)
        assertEquals(2.0, avgPriceProvince)
    }

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
            ), 2.2
        )
        priceRepository.saveAll(listOf(priceOne, priceTO, priceMI))
        explicitFuelRepository.save(ExplicitFuelType(null, "desc", CommonFuelType.GASOLIO))
        val result = priceRepository.findPriceStat(CommonFuelType.GASOLIO, "MI", null)
        assertEquals(2.0, result.getAvg())
        assertEquals(1.8, result.getMin())
        assertEquals(2.2, result.getMax())
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

    @Test
    fun `should find avg price for flags`() {
        val date = Instant.now()
        stationRepository.saveAll(listOf(
            GasStation(1, "mi", "mi", "flag1"),
            GasStation(2, "mi", "mi", "flag2"),
            GasStation(3, "mi", "mi", "flag2"),
            GasStation(4, "mi", "mi", "flag1"),
            GasStation(5, "mi", "mi", "flag2"),
        ))
        priceRepository.saveAll(listOf(
            GasPrice(1.0, 1, date, "diesel"),
            GasPrice(2.1, 2, date, "diesel"),
            GasPrice(1.0, 2, date.plus(1, ChronoUnit.DAYS), "diesel"),
            GasPrice(3.0, 3, date, "diesel"),
            GasPrice(4.0, 4, date, "diesel"),
            GasPrice(5.0, 5, date, "benzi"),
            GasPrice(6.0, 4, date, "benzi"),
            GasPrice(7.0, 3, date, "benzi"),
            GasPrice(9.0, 2, date.plus(1, ChronoUnit.DAYS), "benzi"),
        ))
        explicitFuelRepository.saveAll(listOf(
            ExplicitFuelType("diesel", CommonFuelType.GASOLIO),
            ExplicitFuelType("benzi", CommonFuelType.BENZINA),
        ))
        val avgDiesel = priceRepository.findAvgPriceForFlags(CommonFuelType.GASOLIO)
        assertEquals(2, avgDiesel.size)
        assertEquals(2.0, avgDiesel[0].getPrice())
        assertEquals("flag2", avgDiesel[0].getFlag())
        assertEquals(2.5, avgDiesel[1].getPrice())
        assertEquals("flag1", avgDiesel[1].getFlag())

        val avgBenzina = priceRepository.findAvgPriceForFlags(CommonFuelType.BENZINA)
        assertEquals(2, avgBenzina.size)
        assertEquals(6.0, avgBenzina[0].getPrice())
        assertEquals("flag1", avgBenzina[0].getFlag())
        assertEquals(7.0, avgBenzina[1].getPrice())
        assertEquals("flag2", avgBenzina[1].getFlag())

    }

    @BeforeEach
    fun clear() {
        explicitFuelRepository.deleteAllInBatch()
        priceRepository.deleteAllInBatch()
        stationRepository.deleteAllInBatch()
    }
}
