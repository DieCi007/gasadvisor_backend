package it.gasadvisor.gas_backend.repository

import it.gasadvisor.gas_backend.model.entities.GasStation
import it.gasadvisor.gas_backend.model.entities.GasStationStatus
import it.gasadvisor.gas_backend.model.entities.Municipality
import it.gasadvisor.gas_backend.model.entities.Province
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.TestPropertySource
import java.math.BigInteger

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

    @Test
    fun `should find province with most and least stations`() {
        repository.saveAll(getStations())
        val most = repository.findProvinceWithMostStations()
        assertEquals("MI", most[0].getProvince())
        assertEquals(4, most[0].getTotal())
        assertEquals("TO", most[1].getProvince())
        assertEquals("LE", most[2].getProvince())
        assertEquals("LC", most[3].getProvince())

        val least = repository.findProvinceWithLeastStations()
        assertEquals("MI", least[3].getProvince())
        assertEquals("TO", least[2].getProvince())
        assertEquals("LE", least[1].getProvince())
        assertEquals("LC", least[0].getProvince())
        assertEquals(1, least[0].getTotal())
    }

    @Test
    fun `should find municipality with most and least stations`() {
        repository.saveAll(getStations())
        val most = repository.findMunicipalityWithMostStations()
        assertEquals("torino", most[0].getMunicipality())
        assertEquals(BigInteger.valueOf(3), most[0].getTotal())
        assertTrue(most.stream().noneMatch { m -> m.getMunicipality() == "lecco" })
        assertTrue(most.stream().noneMatch { m -> m.getTotal() == BigInteger.valueOf(1) })

        val least = repository.findMunicipalityWithLeastStations()
        assertEquals(BigInteger.valueOf(1), least[0].getTotal())
        assertTrue(least.stream().noneMatch { m -> m.getProvince() == "TO" })
        assertTrue(least.stream().noneMatch { m -> m.getTotal() == BigInteger.valueOf(3) })
    }

    companion object {
        fun getStations(): List<GasStation> {
            return listOf(
                GasStation(10, "MI", "milano"),
                GasStation(11, "MI", "torino"),
                GasStation(12, "MI", "torino"),
                GasStation(13, "MI", "milano"),
                GasStation(14, "TO", "torino"),
                GasStation(15, "TO", "torino"),
                GasStation(16, "TO", "torino"),
                GasStation(17, "LE", "lecce"),
                GasStation(18, "LE", "lecce"),
                GasStation(19, "LC", "lecco"),
            )
        }
    }


    @BeforeEach
    fun clear() {
        repository.deleteAllInBatch()
        municipalityRepository.deleteAllInBatch()
        provinceRepository.deleteAllInBatch()
    }

}
