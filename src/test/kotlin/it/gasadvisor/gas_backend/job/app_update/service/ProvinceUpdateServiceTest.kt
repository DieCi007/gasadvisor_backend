package it.gasadvisor.gas_backend.job.app_update.service

import it.gasadvisor.gas_backend.model.entities.Province
import it.gasadvisor.gas_backend.repository.GasStationRepository
import it.gasadvisor.gas_backend.repository.ProvinceRepository
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
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
        val province = Province(null, "MI", emptySet())
        val province2 = Province(null, "TO", emptySet())
        whenever(stationRepo.findNotSavedProvinces())
            .thenReturn(listOf(province.name, province2.name))
        val provinceCaptor = ArgumentCaptor.forClass(Province::class.java)
        whenever(provinceRepository.save(any())).then { i -> i.getArgument<Province>(0) }
        service.update()
        verify(provinceRepository, times(2)).save(provinceCaptor.capture())
        assertTrue(provinceCaptor.firstValue.name == "MI")
        assertTrue(provinceCaptor.secondValue.name == "TO")
    }
    private fun <T> any(): T = Mockito.any()
}
