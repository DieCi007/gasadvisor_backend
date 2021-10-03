package it.gasadvisor.gas_backend.job.app_update.service

import it.gasadvisor.gas_backend.api.gas.station.service.GasStationService
import it.gasadvisor.gas_backend.api.gas.unresolved_station.service.UnresolvedGasStationService
import it.gasadvisor.gas_backend.exception.NotFoundException
import it.gasadvisor.gas_backend.model.UnresolvedGasStation
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*
import java.io.BufferedReader
import java.io.InputStreamReader

@ExtendWith(MockitoExtension::class)
internal class GasStationUpdateServiceTest {

    @Mock
    lateinit var stationService: GasStationService

    @Mock
    lateinit var unresolvedStationService: UnresolvedGasStationService
    private val delimiter = ";"
    private lateinit var updateService: GasStationUpdateService

    @BeforeEach
    fun setUp() {
        updateService = GasStationUpdateService(stationService, unresolvedStationService, delimiter)
    }

    @Test
    fun `should save all when correct csv`() {
        val fileStream = this::class.java.classLoader.getResourceAsStream("fixtures/station/station-correct.csv")
            ?: throw NotFoundException("file not found")
        val bufferedReader = BufferedReader(InputStreamReader(fileStream))
        updateService.handle(bufferedReader)

        verify(stationService, times(1)).saveAll(any())
    }

    @Test
    fun `should save bad formatted data in right repo`() {
        val fileStream = this::class.java.classLoader.getResourceAsStream("fixtures/station/station-incorrect.csv")
            ?: throw NotFoundException("file not found")
        val bufferedReader = BufferedReader(InputStreamReader(fileStream))
        whenever(unresolvedStationService.getAll()).thenReturn(emptyList())
        updateService.handle(bufferedReader)
        verify(unresolvedStationService, times(4)).save(any())
        verifyZeroInteractions(stationService)
    }

    @Test
    fun `should save correct bad formatted data`() {
        val fileStream = this::class.java.classLoader.getResourceAsStream("fixtures/station/station-incorrect.csv")
            ?: throw NotFoundException("file not found")
        val bufferedReader = BufferedReader(InputStreamReader(fileStream))
        whenever(unresolvedStationService.getAll()).thenReturn(
            listOf(
                UnresolvedGasStation(
                    "46351DI BENEDETTO CARBURANTI S.R.L.;DBCarburanti;Altro;VILLASETA;VILLASETA S.S.115 KM 186,225;AGRIGENTO;AG;37.29331979071350;13.569776862859700",
                    false
                )
            )
        )
        updateService.handle(bufferedReader)
        verify(unresolvedStationService, times(3)).save(any())
        verifyZeroInteractions(stationService)
    }

}
