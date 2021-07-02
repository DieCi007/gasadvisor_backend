package it.gasadvisor.gas_backend.job.priceupdate.processor

import it.gasadvisor.gas_backend.job.priceupdate.service.MunicipalityUpdateService
import it.gasadvisor.gas_backend.job.priceupdate.service.ProvinceUpdateService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.verify

@ExtendWith(MockitoExtension::class)
internal class ProvinceUpdateProcessorTest {
    @Mock
    lateinit var service: ProvinceUpdateService

    @Mock
    lateinit var municipalityUpdateService: MunicipalityUpdateService

    @InjectMocks
    lateinit var processor: ProvinceUpdateProcessor

    @Test
    fun `should update`() {
        processor.update();
        verify(service).update()
        verify(municipalityUpdateService).update()
    }
}
