package it.gasadvisor.gas_backend.api.gas.explicit_fuel.service

import it.gasadvisor.gas_backend.api.gas.explicit_fuel.contract.AssignFuelRequest
import it.gasadvisor.gas_backend.api.gas.explicit_fuel.contract.IExplicitFuelType
import it.gasadvisor.gas_backend.exception.NotFoundException
import it.gasadvisor.gas_backend.model.entities.ExplicitFuelType
import it.gasadvisor.gas_backend.model.enums.CommonFuelType
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

    fun adminGetAll(): List<IExplicitFuelType> {
        return repo.adminGetAll()
    }

    fun assign(id: Long, request: AssignFuelRequest): IExplicitFuelType {
        var saved = repo.findById(id).orElseThrow { NotFoundException("Explicit fuel type was not found") }
        saved.commonType = request.type
        saved = repo.save(saved)
        return object : IExplicitFuelType {
            override fun getId(): Long? {
                return saved.id
            }

            override fun getName(): String {
                return saved.name
            }

            override fun getType(): CommonFuelType? {
                return saved.commonType
            }

        }
    }
}
