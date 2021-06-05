package it.gasadvisor.gas_backend.job.priceupdate.processor

import it.gasadvisor.gas_backend.job.priceupdate.service.PriceUpdateService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.verify

@ExtendWith(MockitoExtension::class)
internal class PriceUpdateProcessorTest {
    @Mock
    lateinit var gasPriceUpdateService: PriceUpdateService
    private val endpoint = "https://www.mise.gov.it/images/exportCSV/prezzo_alle_8.csv"
    lateinit var processor: PriceUpdateProcessor

    @BeforeEach
    fun setUp() {
        processor = PriceUpdateProcessor(gasPriceUpdateService, endpoint)
    }

    @Test
    fun `should get file stream`() {
        processor.update()
        verify(gasPriceUpdateService).handle(any())
    }
}
