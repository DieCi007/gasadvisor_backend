package it.gasadvisor.gas_backend.job.priceupdate.scheduler

import it.gasadvisor.gas_backend.job.priceupdate.processor.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Qualifier

@ExtendWith(MockitoExtension::class)
internal class JobUpdateGasTest {
    @Mock
    lateinit var stationPriceUpdateProcessor: GasStationUpdateProcessor

    @Mock
    lateinit var priceUpdateProcessor: PriceUpdateProcessor

    @Mock
    lateinit var provinceUpdateProcessor: ProvinceUpdateProcessor

    @Mock
    lateinit var explicitFuelUpdateProcessor: ExplicitFuelUpdateProcessor

    @Mock
    lateinit var gasStatUpdateProcessor: GasStatUpdateProcessor

    @InjectMocks
    lateinit var jobUpdateGasTest: JobUpdateGas

    @Test
    fun `should interact with both processors`() {
        jobUpdateGasTest.init()
        verify(stationPriceUpdateProcessor).update()
        verify(priceUpdateProcessor).update()
        verify(provinceUpdateProcessor).update()
        verify(explicitFuelUpdateProcessor).update()
        verify(gasStatUpdateProcessor).update()
    }

}
