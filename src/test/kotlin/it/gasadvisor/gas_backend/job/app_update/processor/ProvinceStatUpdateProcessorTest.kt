package it.gasadvisor.gas_backend.job.app_update.processor

import it.gasadvisor.gas_backend.job.app_update.service.ProvinceStatUpdateService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.verify

@ExtendWith(MockitoExtension::class)
internal class ProvinceStatUpdateProcessorTest {
    @Mock
    lateinit var service: ProvinceStatUpdateService

    @InjectMocks
    lateinit var processor: ProvinceStatUpdateProcessor

    @Test
    fun `should update`() {
        processor.update()
        verify(service).update()
    }
}
