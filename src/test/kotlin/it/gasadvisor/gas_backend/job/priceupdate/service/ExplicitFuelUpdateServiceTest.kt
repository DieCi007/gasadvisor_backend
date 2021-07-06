package it.gasadvisor.gas_backend.job.priceupdate.service

import it.gasadvisor.gas_backend.model.ExplicitFuelType
import it.gasadvisor.gas_backend.repository.ExplicitFuelRepository
import it.gasadvisor.gas_backend.repository.GasPriceRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
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

    @Test
    fun `should work`() {
        whenever(priceRepository.findNotSavedFuelTypes())
            .thenReturn(listOf("one", "two"))
        val captor = ArgumentCaptor.forClass(ExplicitFuelType::class.java)
        whenever(fuelRepository.save(any())).then { i -> i.getArgument<ExplicitFuelType>(0) }
        service.update()
        verify(fuelRepository, times(2)).save(captor.capture())
        assertEquals(captor.firstValue.name, "one")
        assertEquals(captor.secondValue.name, "two")
    }

    private fun <T> any(): T = Mockito.any()

}
