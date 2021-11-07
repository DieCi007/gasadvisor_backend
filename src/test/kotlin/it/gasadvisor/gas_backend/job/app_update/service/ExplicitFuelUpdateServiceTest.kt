package it.gasadvisor.gas_backend.job.app_update.service

import it.gasadvisor.gas_backend.model.entities.ExplicitFuelType
import it.gasadvisor.gas_backend.repository.ExplicitFuelRepository
import it.gasadvisor.gas_backend.repository.GasPriceRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*

@ExtendWith(MockitoExtension::class)
internal class ExplicitFuelUpdateServiceTest {
    @Mock
    lateinit var fuelRepository: ExplicitFuelRepository

    @Mock
    lateinit var priceRepository: GasPriceRepository

    @InjectMocks
    lateinit var service: ExplicitFuelUpdateService

    @Captor
    lateinit var captor: ArgumentCaptor<List<ExplicitFuelType>>

    @Test
    fun `should work`() {
        whenever(priceRepository.findNotSavedFuelTypes())
            .thenReturn(listOf("one", "two"))
//        whenever(fuelRepository.save(any())).then { i -> i.getArgument<ExplicitFuelType>(0) }
        service.update()
        verify(fuelRepository, times(1)).saveAll(capture(captor))
        assertEquals(captor.firstValue[0].name, "one")
        assertEquals(captor.firstValue[1].name, "two")
    }

    private fun <T> any(): T = Mockito.any()

}
