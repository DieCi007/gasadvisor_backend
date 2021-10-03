package it.gasadvisor.gas_backend.api.gas.station.service

import it.gasadvisor.gas_backend.api.gas.station.contract.UpdateGasStationRequest
import it.gasadvisor.gas_backend.exception.BadRequestException
import it.gasadvisor.gas_backend.model.entities.GasStation
import it.gasadvisor.gas_backend.model.entities.GasStationStatus
import it.gasadvisor.gas_backend.repository.GasPriceRepository
import it.gasadvisor.gas_backend.repository.GasStationRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.AdditionalAnswers.returnsFirstArg
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class GasStationServiceTest {
    @Mock
    lateinit var repository: GasStationRepository

    @Mock
    lateinit var priceRepository: GasPriceRepository

    @InjectMocks
    lateinit var service: GasStationService

    @Test
    fun `should throw bad request`() {
        val request = UpdateGasStationRequest(
            1, "owner", "flag",
            "type", "name", "address", "municipality", "province",
            1.1, 2.2, GasStationStatus.ACTIVE
        )
        val station = request.toGasStation()
        whenever(repository.findById(1)).thenReturn(Optional.of(station))
        assertThrows<BadRequestException> { service.create(request) }
    }

    @Test
    fun `should create station`() {
        val request = UpdateGasStationRequest(
            1, "owner", "flag",
            "type", "name", "address", "municipality", "province",
            1.1, 2.2, GasStationStatus.ACTIVE
        )
        val station = request.toGasStation()
        whenever(repository.findById(1)).thenReturn(Optional.empty())
        whenever(repository.save(any())).then(returnsFirstArg<GasStation>())
        service.create(request)
        val captor = ArgumentCaptor.forClass(GasStation::class.java)
        verify(repository, times(1)).save(captor.capture())
        assertEquals(captor.value.address, station.address)
        assertEquals(captor.value.flag, station.flag)
        assertEquals(captor.value.latitude, station.latitude)
    }


}
