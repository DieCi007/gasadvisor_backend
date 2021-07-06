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
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
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
        val municipalityCaptor = ArgumentCaptor.forClass(Municipality::class.java)
        whenever(municipalityRepository.save(any())).then { i -> i.getArgument<Municipality>(0) }
        service.update()
        verify(municipalityRepository).save(municipalityCaptor.capture())
        assertTrue(municipalityCaptor.value.name == "CO")
    }

    private fun <T> any(): T = Mockito.any()


}
