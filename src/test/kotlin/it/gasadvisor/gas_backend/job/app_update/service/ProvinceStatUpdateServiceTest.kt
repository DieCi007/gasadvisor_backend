package it.gasadvisor.gas_backend.job.app_update.service

import it.gasadvisor.gas_backend.fixtures.IMunicipalityStationsTotalFixture.Companion.getIMunicipalityStationsTotal
import it.gasadvisor.gas_backend.fixtures.IPriceStatFixture.Companion.getIPriceStat
import it.gasadvisor.gas_backend.model.entities.Municipality
import it.gasadvisor.gas_backend.model.entities.PriceStat
import it.gasadvisor.gas_backend.model.entities.Province
import it.gasadvisor.gas_backend.model.entities.ProvinceStat
import it.gasadvisor.gas_backend.repository.*
import it.gasadvisor.gas_backend.repository.contract.IMunicipalityStationsTotal
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.capture
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class ProvinceStatUpdateServiceTest {
    @Mock
    lateinit var provinceStatRepository: ProvinceStatRepository

    @Mock
    lateinit var priceRepository: GasPriceRepository

    @Mock
    lateinit var priceStatRepository: PriceStatRepository

    @Mock
    lateinit var provinceRepository: ProvinceRepository

    @Mock
    lateinit var municipalityRepository: MunicipalityRepository

    @InjectMocks
    lateinit var service: ProvinceStatUpdateService

    @Captor
    lateinit var priceListCaptor: ArgumentCaptor<List<PriceStat>>

    @Test
    fun `update should work`() {
        val province = Province(1, "LE")
        val municipalityMostStations = Municipality(1, "copertino", province)
        val municipalityLeastStations = Municipality(2, "carmiano", province)
        val date = Instant.now().truncatedTo(ChronoUnit.DAYS)
        whenever(provinceRepository.findAll())
            .thenReturn(listOf(province))

        whenever(municipalityRepository.findOneWithLeastStations("LE"))
            .thenReturn(getIMunicipalityStationsTotal("carmiano", "LE"))
        whenever(municipalityRepository.findByNameAndProvince("carmiano", "LE"))
            .thenReturn(Optional.of(municipalityLeastStations))

        whenever(municipalityRepository.findOneWithMostStations("LE"))
            .thenReturn(getIMunicipalityStationsTotal("copertino", "LE"))
        whenever(municipalityRepository.findByNameAndProvince("copertino", "LE"))
            .thenReturn(Optional.of(municipalityMostStations))

        whenever(priceRepository.findPriceStat(any(), any(), any()))
            .thenReturn(getIPriceStat())
        val captor = ArgumentCaptor.forClass(ProvinceStat::class.java)
        whenever(provinceStatRepository.save(any())).then { i -> i.getArgument<ProvinceStat>(0) }
        service.update()
        verify(provinceStatRepository).save(captor.capture())
        assertEquals(date, captor.value.date)
        assertEquals("copertino", captor.value.mostStationsMunicipality?.name)
        assertEquals("carmiano", captor.value.leastStationsMunicipality?.name)
        assertEquals("LE", captor.value.province.name)

        verify(priceStatRepository, times(1)).saveAll(capture(priceListCaptor))
        assertEquals(12, priceListCaptor.value.size)
    }

    private fun <T> any(): T = Mockito.any()

}
