package it.gasadvisor.gas_backend.job.priceupdate.scheduler

import it.gasadvisor.gas_backend.job.priceupdate.processor.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension

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

    @Mock
    lateinit var provinceStatUpdateProcessor: ProvinceStatUpdateProcessor

    @Mock
    lateinit var modifiedStationProcessor: ModifiedGasStationUpdateProcessor

    @InjectMocks
    lateinit var jobUpdateGasTest: JobUpdateGas

    @Test
    fun `should interact with all processors`() {
        jobUpdateGasTest.init()
        verify(stationPriceUpdateProcessor).update()
        verify(priceUpdateProcessor).update()
        verify(provinceUpdateProcessor).update()
        verify(explicitFuelUpdateProcessor).update()
        verify(gasStatUpdateProcessor).update()
        verify(provinceStatUpdateProcessor).update()
        verify(modifiedStationProcessor).update()
    }

}
