package it.gasadvisor.gas_backend.api.province.service

import it.gasadvisor.gas_backend.repository.ProvinceRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ProvinceService @Autowired constructor(
    private val repo: ProvinceRepository
) {
    fun getAll(): List<String> {
        return repo.findAllNames()
    }

}
