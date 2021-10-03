package it.gasadvisor.gas_backend.api.gas.unresolved_station.service

import it.gasadvisor.gas_backend.model.entities.UnresolvedGasStation
import it.gasadvisor.gas_backend.repository.UnresolvedGasStationRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class UnresolvedGasStationServiceTest {
    @Mock
    lateinit var repo: UnresolvedGasStationRepository

    @InjectMocks
    lateinit var service: UnresolvedGasStationService

    @Test
    fun `should resolved station`() {
        whenever(repo.findById(1)).thenReturn(
            Optional.of(UnresolvedGasStation(1, "something", false))
        )
        val captor = ArgumentCaptor.forClass(UnresolvedGasStation::class.java)
        service.resolve(1)
        verify(repo, times(1)).save(captor.capture())
        assertEquals(true, captor.value.isResolved)
    }

}
