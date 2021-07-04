package it.gasadvisor.gas_backend.job.priceupdate.service

import it.gasadvisor.gas_backend.fixtures.IPriceStatFixture.Companion.getIPriceStat
import it.gasadvisor.gas_backend.model.Province
import it.gasadvisor.gas_backend.model.ProvinceStat
import it.gasadvisor.gas_backend.repository.GasPriceRepository
import it.gasadvisor.gas_backend.repository.ProvinceRepository
import it.gasadvisor.gas_backend.repository.ProvinceStatRepository
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
internal class ProvinceStatUpdateServiceTest {
    @Mock
    lateinit var provinceStatRepository: ProvinceStatRepository

    @Mock
    lateinit var priceRepository: GasPriceRepository

    @Mock
    lateinit var provinceRepository: ProvinceRepository

    @InjectMocks
    lateinit var service: ProvinceStatUpdateService

    @Test
    fun `update should work`() {
        val province = Province(1, "LE")
        val date = Instant.now().truncatedTo(ChronoUnit.DAYS)
        whenever(provinceRepository.findAll())
            .thenReturn(listOf(province))
        whenever(priceRepository.findPriceStat(any(), any(), any()))
            .thenReturn(getIPriceStat())
        val captor = ArgumentCaptor.forClass(ProvinceStat::class.java)
        service.update()
        verify(provinceStatRepository).save(captor.capture())
        assertEquals(date, captor.value.date)
        assertEquals("LE", captor.value.province.name)
        assertEquals(12, captor.value.prices.size)
    }

    private fun <T> any(): T = Mockito.any()

}
