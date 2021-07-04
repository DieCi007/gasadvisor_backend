package it.gasadvisor.gas_backend.repository

import it.gasadvisor.gas_backend.model.GasStation
import it.gasadvisor.gas_backend.model.Municipality
import it.gasadvisor.gas_backend.model.Province
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@JpaTest
class MunicipalityRepositoryTest @Autowired constructor(
    val municipalityRepository: MunicipalityRepository,
    val gasStationRepository: GasStationRepository,
    val provinceRepository: ProvinceRepository
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
    fun `should load province`() {
        var province = Province(null, "province")
        province = provinceRepository.save(province)
        val municipality = Municipality(null, "muni", province)
        municipalityRepository.save(municipality)
        val res = municipalityRepository
            .findByNameAndProvince("muni", "province").get()
        assertEquals("province", res.province.name)
    }


    @Test
    fun `should find municipality with most stations`() {
        gasStationRepository.saveAll(stations)
        val result = municipalityRepository.findOneWithMostStations()
        assertEquals("CO", result.getMunicipality())
        assertEquals(3, result.getTotal())
    }

    @Test
    fun `should find municipality with least stations`() {
        gasStationRepository.saveAll(stations)
        val result = municipalityRepository.findOneWithLeastStations()
        assertEquals("MI", result.getMunicipality())
        assertEquals(1, result.getTotal())
    }

    @BeforeEach
    fun clear() {
        municipalityRepository.deleteAllInBatch()
        gasStationRepository.deleteAllInBatch()
        provinceRepository.deleteAllInBatch()
    }
}
