package it.gasadvisor.gas_backend.api.municipality.service

import it.gasadvisor.gas_backend.repository.MunicipalityRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MunicipalityService @Autowired constructor(
    private val repo: MunicipalityRepository
) {

    fun getNamesByProvince(provinceName: String): List<String> {
        return repo.findNamesByProvince(provinceName);
    }
}
