package it.gasadvisor.gas_backend.job.priceupdate.service

import it.gasadvisor.gas_backend.model.Municipality
import it.gasadvisor.gas_backend.model.Province
import it.gasadvisor.gas_backend.repository.GasStationRepository
import it.gasadvisor.gas_backend.repository.MunicipalityRepository
import it.gasadvisor.gas_backend.repository.ProvinceRepository
import it.gasadvisor.gas_backend.repository.contract.IMunicipalityProvince
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*
import java.util.*

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
        service.update()
        verify(provinceRepository, times(2)).save(provinceCaptor.capture())
        assertTrue(provinceCaptor.firstValue.name == "MI")
        assertTrue(provinceCaptor.secondValue.name == "TO")
    }
}