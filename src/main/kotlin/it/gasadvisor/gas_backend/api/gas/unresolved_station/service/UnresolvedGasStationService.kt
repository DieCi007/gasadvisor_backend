package it.gasadvisor.gas_backend.api.gas.unresolved_station.service

import it.gasadvisor.gas_backend.api.gas.unresolved_station.contract.ChangeUnresolvedStationRequest
import it.gasadvisor.gas_backend.api.gas.unresolved_station.contract.GetAllUnresolvedResponse
import it.gasadvisor.gas_backend.exception.NotFoundException
import it.gasadvisor.gas_backend.model.entities.UnresolvedGasStation
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

    fun getAll(): List<GetAllUnresolvedResponse> {
        return repo.findAll().map { GetAllUnresolvedResponse.fromUnresolvedStation(it) }
    }

    fun resolve(id: Long, request: ChangeUnresolvedStationRequest) {
        val saved = repo.findById(id).orElseThrow { NotFoundException("Could not find this unresolved station") }
        saved.isResolved = request.isResolved
        repo.save(saved)
    }
}
