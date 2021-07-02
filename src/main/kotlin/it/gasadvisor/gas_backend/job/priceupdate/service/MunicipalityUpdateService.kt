package it.gasadvisor.gas_backend.job.priceupdate.service

import it.gasadvisor.gas_backend.model.Municipality
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

    override fun save(feature: Municipality) {
        municipalityRepository.save(feature)
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