package it.gasadvisor.gas_backend.api.gas.explicit_fuel.service

import it.gasadvisor.gas_backend.model.entities.ExplicitFuelType
import it.gasadvisor.gas_backend.repository.ExplicitFuelRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ExplicitFuelService @Autowired constructor(
    private val repo: ExplicitFuelRepository
) {
    fun getAll(): List<ExplicitFuelType> {
        return repo.findAll()
    }
}
