package it.gasadvisor.gas_backend.api.gas.price.service

import it.gasadvisor.gas_backend.model.entities.*
import it.gasadvisor.gas_backend.model.enums.CommonFuelType
import it.gasadvisor.gas_backend.model.enums.PriceStatType
import it.gasadvisor.gas_backend.repository.GasPriceRepository
import it.gasadvisor.gas_backend.repository.GasStatRepository
import it.gasadvisor.gas_backend.repository.PriceStatRepository
import it.gasadvisor.gas_backend.repository.ProvinceStatRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class GasPriceServiceTest {
    @Mock
    lateinit var repository: GasPriceRepository

    @Mock
    lateinit var priceStatRepo: PriceStatRepository

    @Mock
    lateinit var gasStatRepo: GasStatRepository

    @Mock
    lateinit var provinceStatRepo: ProvinceStatRepository

    @InjectMocks
    lateinit var service: GasPriceService

    @Test
    fun `should get correct stats`() {
        val today = Instant.now().truncatedTo(ChronoUnit.DAYS)
        val provinceMI = Province("MI")
        val provinceTO = Province("TO")
        val priceStats = listOf(
            PriceStat(CommonFuelType.GASOLIO, 1.1, PriceStatType.MIN)
        )
        whenever(gasStatRepo.findByDate(today)).thenReturn(
            Optional.of(
                GasStat(
                    null, today, provinceMI,
                    Municipality(null, "milano", provinceMI),
                    provinceTO, Municipality(null, "corsico", provinceMI),
                    priceStats
                )
            )
        )
        val res = service.getAppStats(null, Instant.now())
        assertEquals("milano", res.mostStationsMunicipality?.municipality)
        assertEquals("MI", res.mostStationsProvince)
        assertEquals("corsico", res.leastStationsMunicipality?.municipality)
        assertEquals("TO", res.leastStationsProvince)
        assertEquals(1, res.priceStats.size)
    }

    @Test
    fun `should get correct stats for province`() {
        val today = Instant.now().truncatedTo(ChronoUnit.DAYS)
        val provinceMI = Province("MI")
        val priceStats = listOf(
            PriceStat(CommonFuelType.GASOLIO, 1.1, PriceStatType.MIN),
            PriceStat(CommonFuelType.GASOLIO, 1.1, PriceStatType.AVG),
        )
        whenever(provinceStatRepo.findByDateAndProvinceName(today, "MI")).thenReturn(
            listOf(
                ProvinceStat(
                    null, today, priceStats,
                    Municipality(null, "milano", provinceMI),
                    Municipality(null, "corsico", provinceMI),
                    provinceMI
                )
            )
        )
        val res = service.getAppStats("MI", Instant.now())
        assertNull(res.mostStationsProvince)
        assertEquals("milano", res.mostStationsMunicipality?.municipality)
        assertEquals("corsico", res.leastStationsMunicipality?.municipality)
        assertEquals(2, res.priceStats.size)
    }

}
