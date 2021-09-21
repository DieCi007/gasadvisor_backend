package it.gasadvisor.gas_backend.repository

import it.gasadvisor.gas_backend.model.GasStation
import it.gasadvisor.gas_backend.model.GasStationStatus
import it.gasadvisor.gas_backend.model.Municipality
import it.gasadvisor.gas_backend.model.Province
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.TestPropertySource

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("classpath:application-test.properties")
class GasStationRepositoryTest @Autowired constructor(
    val repository: GasStationRepository,
    val provinceRepository: ProvinceRepository,
    val municipalityRepository: MunicipalityRepository
) {
    private val stationOne = GasStation(
        1, "diego", "", "", "",
        "", "CI", "MI", 1.1, 1.1, GasStationStatus.ACTIVE
    )
    private val stationTwo = GasStation(
        1, "mario", "", "", "",
        "", "CI", "MI", 1.1, 1.1, GasStationStatus.ACTIVE
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
        assertTrue(res.stream().anyMatch { s -> s.id == 1L })
    }

    @Test
    fun `should get distinct provinces`() {
        repository.saveAll(listOf(stationOne, stationTwo))
        val res = repository.findAllProvinces()
        assertTrue(res.size == 1)
    }

    @Test
    fun `should get not saved provinces`() {
        val province = Province(null, "LE", emptySet())
        val stationMI = GasStation(
            3, "diego", "", "", "",
            "", "", "MI", 1.1, 1.1, GasStationStatus.ACTIVE
        )
        val stationLE = GasStation(
            2, "diego", "", "", "",
            "", "", "LE", 1.1, 1.1, GasStationStatus.ACTIVE
        )
        provinceRepository.save(province)
        repository.saveAll(listOf(stationOne, stationMI, stationLE))
        val res = repository.findNotSavedProvinces()
        assertTrue(res.size == 1)
        assertTrue(res[0] == "MI")
    }

    @Test
    fun `should get not saved municipalities`() {
        var province = Province(null, "LE", emptySet())

        val stationCO = GasStation(
            3, "diego", "", "", "",
            "", "CO", "MI", 1.1, 1.1, GasStationStatus.ACTIVE
        )
        val stationPR = GasStation(
            2, "diego", "", "", "",
            "", "PR", "LE", 1.1, 1.1, GasStationStatus.ACTIVE
        )

        val stationPR2 = GasStation(
            4, "diego", "", "", "",
            "", "PR", "LE", 1.1, 1.1, GasStationStatus.ACTIVE
        )
        val stationPR3 = GasStation(
            5, "diego", "", "", "",
            "", "PR", "MI", 1.1, 1.1, GasStationStatus.ACTIVE
        )
        province = provinceRepository.save(province)
        val municipality = Municipality(null, "CO", province)
        municipalityRepository.save(municipality)
        repository.saveAll(listOf(stationOne, stationCO, stationPR, stationPR2, stationPR3))
        val res = repository.findNotSavedMunicipalities()
        assertTrue(res.size == 3)
        assertTrue(res.stream().map { mp -> mp.getMunicipality() }
            .allMatch { m -> listOf("CI", "PR").contains(m) })
    }

    @BeforeEach
    fun clear() {
        repository.deleteAllInBatch()
        municipalityRepository.deleteAllInBatch()
        provinceRepository.deleteAllInBatch()
    }

}
