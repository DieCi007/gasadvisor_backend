package it.gasadvisor.gas_backend.repository

import it.gasadvisor.gas_backend.model.entities.GasStation
import it.gasadvisor.gas_backend.model.entities.GasStationStatus
import it.gasadvisor.gas_backend.model.entities.ModifiedGasStation
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@JpaTest
class ModifiedGasStationRepositoryTest @Autowired constructor(
    val stationRepo: GasStationRepository,
    val modifiedStationRepo: ModifiedGasStationRepository
) {

    private val station = GasStation(1, "MI", "CO")
    private val modifiedStation = ModifiedGasStation(
        10, station, GasStationStatus.INACTIVE,
        "diego", "flag", "type", "name", "address", "municipality",
        "province", 10.11, 11.22
    )

    @Test
    fun `save should work`() {
        stationRepo.save(station)
        modifiedStationRepo.save(modifiedStation)
        val stations = modifiedStationRepo.findAll()
        assertEquals(1, stations.size)
    }

    @Test
    fun `updating station from modified one should work`() {
        stationRepo.save(station)
        modifiedStationRepo.save(modifiedStation)

        val mStation = modifiedStationRepo.findAll()[0]
        val station = mStation.station
        station.municipality = mStation.municipality!!
        station.province = mStation.province!!
        stationRepo.save(station)
        val updatedStation = stationRepo.findAll()[0]
        assertEquals("province", updatedStation.province)
        assertEquals("municipality", updatedStation.municipality)
    }

    @BeforeEach
    fun clear() {
        stationRepo.deleteAllInBatch()
        modifiedStationRepo.deleteAllInBatch()
    }
}
