package it.gasadvisor.gas_backend.api.gas.unresolved_station.service

import it.gasadvisor.gas_backend.model.UnresolvedGasStation
import it.gasadvisor.gas_backend.repository.UnresolvedGasStationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UnresolvedGasStationService @Autowired constructor(
    private val repo: UnresolvedGasStationRepository
) {
    fun save(station: UnresolvedGasStation): UnresolvedGasStation {
        return repo.save(station)
    }

    fun getUnresolved(): List<UnresolvedGasStation> {
        return repo.findByResolved(false)
    }

    fun getAll(): List<UnresolvedGasStation> {
        return repo.findAll()
    }
}
