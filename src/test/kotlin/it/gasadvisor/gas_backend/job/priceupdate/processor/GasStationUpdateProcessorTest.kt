package it.gasadvisor.gas_backend.job.priceupdate.processor

import it.gasadvisor.gas_backend.job.priceupdate.service.GasStationUpdateService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any

@ExtendWith(MockitoExtension::class)
internal class GasStationUpdateProcessorTest {
    @Mock
    lateinit var gasStationService: GasStationUpdateService

    @InjectMocks
    lateinit var processor: GasStationUpdateProcessor

    @Test
    fun `should get file stream`() {
        processor.endpoint = "https://www.mise.gov.it/images/exportCSV/anagrafica_impianti_attivi.csv"
        processor.update()
        verify(gasStationService).handle(any())
    }
}
