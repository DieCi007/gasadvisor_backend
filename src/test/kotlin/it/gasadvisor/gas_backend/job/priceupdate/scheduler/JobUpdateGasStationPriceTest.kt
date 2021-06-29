package it.gasadvisor.gas_backend.job.priceupdate.scheduler

import it.gasadvisor.gas_backend.job.priceupdate.processor.GasStationUpdateProcessor
import it.gasadvisor.gas_backend.job.priceupdate.processor.PriceUpdateProcessor
import it.gasadvisor.gas_backend.job.priceupdate.processor.ProvinceUpdateProcessor
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class JobUpdateGasStationPriceTest {
    @Mock
    lateinit var stationPriceUpdateProcessor: GasStationUpdateProcessor

    @Mock
    lateinit var priceUpdateProcessor: PriceUpdateProcessor

    @Mock
    lateinit var provinceUpdateProcessor: ProvinceUpdateProcessor

    @InjectMocks
    lateinit var jobUpdateGasStationPriceTest: JobUpdateGasStationPrice

    @Test
    fun `should interact with both processors`() {
        jobUpdateGasStationPriceTest.init()
        verify(stationPriceUpdateProcessor).update()
        verify(priceUpdateProcessor).update()
        verify(provinceUpdateProcessor).update()
    }

}
