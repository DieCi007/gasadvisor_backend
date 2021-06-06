package it.gasadvisor.gas_backend.api.gas.station.service

import it.gasadvisor.gas_backend.api.gas.station.contract.GetAllStationsResponse
import it.gasadvisor.gas_backend.model.GasStation
import it.gasadvisor.gas_backend.repository.GasStationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GasStationService @Autowired constructor(
    private val repository: GasStationRepository
) {

    @Transactional
    fun saveAll(stations: List<GasStation>) {
        repository.saveAll(stations)
    }

    fun findAllLocations(): List<GetAllStationsResponse> {
        return repository.findAllLocations()
    }

}
