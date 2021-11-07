package it.gasadvisor.gas_backend.job.app_update.service

import it.gasadvisor.gas_backend.model.entities.Municipality
import it.gasadvisor.gas_backend.repository.GasStationRepository
import it.gasadvisor.gas_backend.repository.MunicipalityRepository
import it.gasadvisor.gas_backend.repository.ProvinceRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
@Qualifier("municipalityUpdateService")
class MunicipalityUpdateService @Autowired constructor(
    private val stationRepository: GasStationRepository,
    private val municipalityRepository: MunicipalityRepository,
    private val provinceRepository: ProvinceRepository
) : StatUpdateService<Municipality>() {

    override fun save(features: List<Municipality>) {
        municipalityRepository.saveAll(features)
    }

    override fun buildFeatures(): List<Municipality> {
        val unsavedMunicipalities = stationRepository.findNotSavedMunicipalities()
        val municipalitiesWithProvinces = mutableListOf<Municipality>()
        unsavedMunicipalities.forEach { m ->
            val province = provinceRepository.findByName(m.getProvince())
            if (province.isPresent) {
                municipalitiesWithProvinces.add(Municipality(null, m.getMunicipality(), province.get()))
            }
        }
        return municipalitiesWithProvinces
    }
}
