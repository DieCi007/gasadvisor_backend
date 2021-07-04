package it.gasadvisor.gas_backend.repository

import it.gasadvisor.gas_backend.model.GasStation
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.TestPropertySource

@JpaTest
class MunicipalityRepositoryTest @Autowired constructor(
    val municipalityRepository: MunicipalityRepository,
    val gasStationRepository: GasStationRepository
) {
    val stations = listOf(
        GasStation(1, "MI", "CO"),
        GasStation(2, "MI", "CO"),
        GasStation(3, "TO", "CO"),
        GasStation(4, "TO", "CO"),
        GasStation(5, "MI", "CO"),
        GasStation(6, "TO", "MI"),
        GasStation(7, "MI", "MI"),
        GasStation(8, "CR", "MI")
    )

    @Test
    fun `should find municipality with most stations`() {
        gasStationRepository.saveAll(stations)
        val result = municipalityRepository.findOneWithMostStations()
        Assertions.assertEquals("CO", result.getMunicipality())
        Assertions.assertEquals(3, result.getTotal())
    }

    @Test
    fun `should find municipality with least stations`() {
        gasStationRepository.saveAll(stations)
        val result = municipalityRepository.findOneWithLeastStations()
        Assertions.assertEquals("MI", result.getMunicipality())
        Assertions.assertEquals(1, result.getTotal())
    }

    @BeforeEach
    fun clear() {
        municipalityRepository.deleteAllInBatch()
        gasStationRepository.deleteAllInBatch()
    }
}
