package it.gasadvisor.gas_backend.job.app_update.service

import it.gasadvisor.gas_backend.model.entities.Municipality
import it.gasadvisor.gas_backend.model.entities.Province
import it.gasadvisor.gas_backend.repository.GasStationRepository
import it.gasadvisor.gas_backend.repository.MunicipalityRepository
import it.gasadvisor.gas_backend.repository.ProvinceRepository
import it.gasadvisor.gas_backend.repository.contract.IMunicipalityProvince
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class MunicipalityUpdateServiceTest {
    @Mock
    lateinit var stationRepository: GasStationRepository

    @Mock
    lateinit var municipalityRepository: MunicipalityRepository

    @Mock
    lateinit var provinceRepository: ProvinceRepository

    @InjectMocks
    lateinit var service: MunicipalityUpdateService

    @Captor
    lateinit var captor: ArgumentCaptor<List<Municipality>>

    @Test
    fun `update should work`() {
        val province = Province(null, "MI", emptySet())
        whenever(stationRepository.findNotSavedMunicipalities())
            .thenReturn(listOf(object : IMunicipalityProvince {
                override fun getMunicipality(): String {
                    return "CO"
                }

                override fun getProvince(): String {
                    return "MI"
                }
            }))
        whenever(provinceRepository.findByName("MI"))
            .thenReturn(Optional.of(province))

        service.update()
        verify(municipalityRepository, times(1)).saveAll(capture(captor))
        assertTrue(captor.firstValue[0].name == "CO")
    }

    private fun <T> any(): T = Mockito.any()


}
