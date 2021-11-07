package it.gasadvisor.gas_backend.job.app_update.service

import it.gasadvisor.gas_backend.model.entities.GasStation
import it.gasadvisor.gas_backend.model.entities.GasStationStatus
import it.gasadvisor.gas_backend.model.entities.ModifiedGasStation
import it.gasadvisor.gas_backend.repository.GasStationRepository
import it.gasadvisor.gas_backend.repository.ModifiedGasStationRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.capture
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

    @Captor
    lateinit var captor: ArgumentCaptor<List<GasStation>>

    @Test
    fun `update should work`() {
        val station = GasStation(1, "MI", "CO")
        val modifiedStation = ModifiedGasStation(
            10, station, GasStationStatus.INACTIVE,
            "diego", "flag", "type", "name", "address", "municipality",
            "province", 10.11, 11.22
        )
        whenever(modifiedStationRepo.findAll()).thenReturn(listOf(modifiedStation))
//        whenever(stationRepo.save(any())).then {i -> i.getArgument<GasStation>(0)}
        service.update()
        verify(stationRepo).saveAll(capture(captor))
        assertEquals("province", captor.value[0].province)
        assertEquals("diego", captor.value[0].owner)
        assertEquals("flag", captor.value[0].flag)
        assertEquals("type", captor.value[0].type)
        assertEquals("name", captor.value[0].name)
    }

    fun <T> any(): T = Mockito.any()
}
