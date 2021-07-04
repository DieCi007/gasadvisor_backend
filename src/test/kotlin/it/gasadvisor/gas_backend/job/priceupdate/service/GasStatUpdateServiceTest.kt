package it.gasadvisor.gas_backend.job.priceupdate.service

import it.gasadvisor.gas_backend.model.GasStat
import it.gasadvisor.gas_backend.model.Municipality
import it.gasadvisor.gas_backend.model.Province
import it.gasadvisor.gas_backend.repository.GasPriceRepository
import it.gasadvisor.gas_backend.repository.GasStatRepository
import it.gasadvisor.gas_backend.repository.MunicipalityRepository
import it.gasadvisor.gas_backend.repository.ProvinceRepository
import it.gasadvisor.gas_backend.repository.contract.IMunicipalityStationsTotal
import it.gasadvisor.gas_backend.repository.contract.IPriceStat
import it.gasadvisor.gas_backend.repository.contract.IProvinceStationsTotal
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
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class GasStatUpdateServiceTest {
    @Mock
    lateinit var gasStatRepository: GasStatRepository

    @Mock
    lateinit var gasPriceRepository: GasPriceRepository

    @Mock
    lateinit var provinceRepository: ProvinceRepository

    @Mock
    lateinit var municipalityRepository: MunicipalityRepository

    @InjectMocks
    lateinit var service: GasStatUpdateService

    @Test
    fun `update should work`() {
        val province = Province(1, "LE")
        val date = Instant.now().truncatedTo(ChronoUnit.DAYS)
        val municipality = Municipality(1, "LE", province)
        whenever(gasPriceRepository.findPriceStat(any(), any(), any()))
            .thenReturn(getIPriceStat())
        whenever(provinceRepository.findOneWithMostStations())
            .thenReturn(getIProvinceStationsTotal("MI"))
        whenever(provinceRepository.findByName("MI"))
            .thenReturn(Optional.empty())
        whenever(provinceRepository.findOneWithLeastStations())
            .thenReturn(getIProvinceStationsTotal("LE"))
        whenever(provinceRepository.findByName("LE"))
            .thenReturn(Optional.empty())
        whenever(municipalityRepository.findOneWithMostStations())
            .thenReturn(getIMunicipalityStationsTotal("MI", "MI"))
        whenever(municipalityRepository.findByNameAndProvince("MI", "MI"))
            .thenReturn(Optional.empty())
        whenever(municipalityRepository.findOneWithLeastStations())
            .thenReturn(getIMunicipalityStationsTotal("LE", "LE"))
        whenever(municipalityRepository.findByNameAndProvince("LE", "LE"))
            .thenReturn(Optional.of(municipality))
        val captor = ArgumentCaptor.forClass(GasStat::class.java)
        service.update()
        verify(gasStatRepository).save(captor.capture())
        assertEquals(date, captor.value.date)
        assertEquals("LE", captor.value.leastStationsMunicipality?.name)
        assertEquals(12, captor.value.prices.size)
    }

    //    private fun <T> any(type: Class<T>): T = Mockito.any(type)
    //inline fun <reified T> anyNonNull(): T = Mockito.any<T>(T::class.java)
    private fun <T> any(): T = Mockito.any()

    private fun getIMunicipalityStationsTotal(municipality: String, province: String): IMunicipalityStationsTotal {
        return object : IMunicipalityStationsTotal {
            override fun getMunicipality(): String {
                return municipality
            }

            override fun getProvince(): String {
                return province
            }

            override fun getTotal(): Long {
                return 1
            }

        }
    }

    private fun getIProvinceStationsTotal(province: String): IProvinceStationsTotal {
        return object : IProvinceStationsTotal {
            override fun getProvince(): String {
                return province
            }

            override fun getTotal(): Long {
                return 1
            }

        }
    }

    private fun getIPriceStat(): IPriceStat {
        return object : IPriceStat {
            override fun getAvg(): Double {
                return 2.50
            }

            override fun getMin(): Double {
                return 2.00
            }

            override fun getMax(): Double {
                return 3.00
            }
        }
    }
}
