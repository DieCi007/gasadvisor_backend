package it.gasadvisor.gas_backend.job.app_update.service

import it.gasadvisor.gas_backend.model.entities.Province
import it.gasadvisor.gas_backend.repository.GasStationRepository
import it.gasadvisor.gas_backend.repository.ProvinceRepository
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*

@ExtendWith(MockitoExtension::class)
internal class ProvinceUpdateServiceTest {
    @Mock
    lateinit var stationRepo: GasStationRepository

    @Mock
    lateinit var provinceRepository: ProvinceRepository

    @InjectMocks
    lateinit var service: ProvinceUpdateService


    @Test
    fun `update should work`() {
        val captor = argumentCaptor<List<Province>>()
        val province = Province(null, "MI", emptySet())
        val province2 = Province(null, "TO", emptySet())
        whenever(stationRepo.findNotSavedProvinces())
            .thenReturn(listOf(province.name, province2.name))
        service.update()
        verify(provinceRepository, times(1)).saveAll(captor.capture())
        assertTrue(captor.firstValue[0].name == "MI")
        assertTrue(captor.firstValue[1].name == "TO")
    }

    private fun <T> any(): T = Mockito.any()
    private fun <T> anyList(): List<T> = Mockito.anyList()
}
