package it.gasadvisor.gas_backend.repository

import it.gasadvisor.gas_backend.api.gas.station.contract.GetAllStationsResponse
import it.gasadvisor.gas_backend.model.GasStation
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.TestPropertySource

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("classpath:application-test.properties")
class GasStationRepositoryTest @Autowired constructor(
    val repository: GasStationRepository
) {
    private val stationOne = GasStation(
        1, "diego", "", "", "",
        "", "", "", 1.1, 1.1,
    )
    private val stationTwo = GasStation(
        1, "mario", "", "", "",
        "", "", "", 1.1, 1.1,
    )

    @Test
    fun `save should update station`() {
        repository.save(stationOne)
        val second = repository.save(stationTwo)
        val fromDb = repository.findById(1).get()
        assertEquals(second.owner, fromDb.owner)
    }

    @Test
    fun `should get all station locations`() {
        repository.save(stationOne)
        val res = repository.findAllLocations()
        println(res)
        assertTrue(res.stream().anyMatch { s -> s.id == 1L })
    }

}
