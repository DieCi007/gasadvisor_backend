package it.gasadvisor.gas_backend.job.app_update.processor

import it.gasadvisor.gas_backend.job.app_update.service.GasStationUpdateService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.verify

@ExtendWith(MockitoExtension::class)
internal class GasStationUpdateProcessorTest {
    @Mock
    lateinit var gasStationService: GasStationUpdateService
    private val endpoint = "https://www.mise.gov.it/images/exportCSV/anagrafica_impianti_attivi.csv"
    lateinit var processor: GasStationUpdateProcessor

    @BeforeEach
    fun setUp() {
        processor = GasStationUpdateProcessor(gasStationService, endpoint)
    }

    @Test
    fun `should get file stream`() {
        processor.update()
        verify(gasStationService).handle(any())
    }
}
