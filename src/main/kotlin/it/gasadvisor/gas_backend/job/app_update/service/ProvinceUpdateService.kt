package it.gasadvisor.gas_backend.job.app_update.service

import it.gasadvisor.gas_backend.model.Province
import it.gasadvisor.gas_backend.repository.GasStationRepository
import it.gasadvisor.gas_backend.repository.ProvinceRepository
import it.gasadvisor.gas_backend.util.Log
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ProvinceUpdateService @Autowired constructor(
    private val stationRepository: GasStationRepository,
    private val provinceRepository: ProvinceRepository,
) : StatUpdateService<Province>() {
    companion object : Log()

    override fun save(feature: Province): Province {
        return provinceRepository.save(feature)
    }

    override fun buildFeatures(): List<Province> {
        val unsavedProvinces = stationRepository.findNotSavedProvinces()
        return unsavedProvinces.map { Province(null, it) }
    }

}
