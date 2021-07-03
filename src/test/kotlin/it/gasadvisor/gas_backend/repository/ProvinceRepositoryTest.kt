package it.gasadvisor.gas_backend.repository

import it.gasadvisor.gas_backend.model.GasStation
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.TestPropertySource

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("classpath:application-test.properties")
class ProvinceRepositoryTest @Autowired constructor(
    val provinceRepository: ProvinceRepository,
    val gasStationRepository: GasStationRepository
) {
    val stations = listOf(
        GasStation(1, "MI"),
        GasStation(2, "MI"),
        GasStation(3, "TO"),
        GasStation(4, "TO"),
        GasStation(5, "MI"),
        GasStation(6, "RO")
    )

    @Test
    fun `should find province with most stations`() {
        gasStationRepository.saveAll(stations)
        val result = provinceRepository.findOneWithMostStations()
        assertEquals("MI", result.getProvince())
        assertEquals(3, result.getTotal())
    }

    @Test
    fun `should find province with least stations`() {
        gasStationRepository.saveAll(stations)
        val result = provinceRepository.findOneWithLeastStations()
        assertEquals("RO", result.getProvince())
        assertEquals(1, result.getTotal())
    }

    @BeforeEach
    fun clear() {
        provinceRepository.deleteAllInBatch()
        gasStationRepository.deleteAllInBatch()
    }
}
