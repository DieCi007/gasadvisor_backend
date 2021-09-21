package it.gasadvisor.gas_backend.job.priceupdate.service

import it.gasadvisor.gas_backend.model.GasStation
import it.gasadvisor.gas_backend.model.GasStationStatus
import it.gasadvisor.gas_backend.model.ModifiedGasStation
import it.gasadvisor.gas_backend.repository.GasStationRepository
import it.gasadvisor.gas_backend.repository.ModifiedGasStationRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever


@ExtendWith(MockitoExtension::class)
internal class ModifiedGasStationUpdateServiceTest {
    @Mock
    lateinit var stationRepo: GasStationRepository

    @Mock
    lateinit var modifiedStationRepo: ModifiedGasStationRepository

    @InjectMocks
    lateinit var service: ModifiedGasStationUpdateService


    @Test
    fun `update should work`() {
        val station = GasStation(1, "MI", "CO")
        val modifiedStation = ModifiedGasStation(
            10, station, GasStationStatus.INACTIVE,
            "diego", "flag", "type", "name", "address", "municipality",
            "province", 10.11, 11.22
        )
        whenever(modifiedStationRepo.findAll()).thenReturn(listOf(modifiedStation))
        val captor = ArgumentCaptor.forClass(GasStation::class.java)
        whenever(stationRepo.save(any())).then {i -> i.getArgument<GasStation>(0)}
        service.update()
        verify(stationRepo).save(captor.capture())
        assertEquals("province", captor.value.province)
        assertEquals("diego", captor.value.owner)
        assertEquals("flag", captor.value.flag)
        assertEquals("type", captor.value.type)
        assertEquals("name", captor.value.name)
    }

    fun <T> any(): T = Mockito.any()
}
