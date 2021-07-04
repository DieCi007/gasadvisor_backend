package it.gasadvisor.gas_backend.job.priceupdate.service

import it.gasadvisor.gas_backend.fixtures.IPriceStatFixture.Companion.getIPriceStat
import it.gasadvisor.gas_backend.model.Municipality
import it.gasadvisor.gas_backend.model.MunicipalityStat
import it.gasadvisor.gas_backend.model.Province
import it.gasadvisor.gas_backend.repository.GasPriceRepository
import it.gasadvisor.gas_backend.repository.MunicipalityRepository
import it.gasadvisor.gas_backend.repository.MunicipalityStatRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.Instant
import java.time.temporal.ChronoUnit

@ExtendWith(MockitoExtension::class)
internal class MunicipalityStatUpdateServiceTest {
    @Mock
    lateinit var municipalityStatRepository: MunicipalityStatRepository

    @Mock
    lateinit var priceRepository: GasPriceRepository

    @Mock
    lateinit var municipalityRepository: MunicipalityRepository

    @InjectMocks
    lateinit var service: MunicipalityStatUpdateService

    @Test
    fun `update should work`() {
        val date = Instant.now().truncatedTo(ChronoUnit.DAYS)
        val province = Province(1, "LE")
        val municipality = Municipality(null, "muni", province)
        whenever(municipalityRepository.findAll())
            .thenReturn(listOf(municipality))
        whenever(priceRepository.findPriceStat(any(), any(), any()))
            .thenReturn(getIPriceStat())
        val captor = ArgumentCaptor.forClass(MunicipalityStat::class.java)
        service.update()
        verify(municipalityStatRepository).save(captor.capture())
        assertEquals(date, captor.value.date)
        assertEquals("muni", captor.value.municipality.name)
        assertEquals("LE", captor.value.municipality.province.name)
    }

    private fun <T> any(): T = Mockito.any()

}
