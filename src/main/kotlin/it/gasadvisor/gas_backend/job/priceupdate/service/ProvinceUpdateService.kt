package it.gasadvisor.gas_backend.job.priceupdate.service

import it.gasadvisor.gas_backend.model.Municipality
import it.gasadvisor.gas_backend.model.Province
import it.gasadvisor.gas_backend.repository.GasStationRepository
import it.gasadvisor.gas_backend.repository.MunicipalityRepository
import it.gasadvisor.gas_backend.repository.ProvinceRepository
import it.gasadvisor.gas_backend.util.Log
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ProvinceUpdateService @Autowired constructor(
    private val stationRepository: GasStationRepository,
    private val provinceRepository: ProvinceRepository,
    private val municipalityRepository: MunicipalityRepository
) {
    companion object : Log()

    fun update() {
        val timeInit = System.currentTimeMillis()
        val unsavedProvinces = stationRepository.findNotSavedProvinces()
        unsavedProvinces.forEach { p -> provinceRepository.save(Province(null, p)) }

        val unsavedMunicipalities = stationRepository.findNotSavedMunicipalities()
        unsavedMunicipalities.forEach { m ->
            val province = provinceRepository.findByName(m.getProvince())
            if (province.isPresent) {
                municipalityRepository.save(Municipality(null, m.getMunicipality(), province.get()))
            }
        }

        log.info("Operation completed in ${(System.currentTimeMillis() - timeInit) / 1000} seconds")
    }
}
