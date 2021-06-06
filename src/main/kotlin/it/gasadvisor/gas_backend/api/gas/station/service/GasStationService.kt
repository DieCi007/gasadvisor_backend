package it.gasadvisor.gas_backend.api.gas.station.service

import it.gasadvisor.gas_backend.api.gas.station.contract.GetAllStationsResponse
import it.gasadvisor.gas_backend.api.gas.station.contract.GetStationDataResponse
import it.gasadvisor.gas_backend.api.gas.station.contract.GetStationPriceResponse
import it.gasadvisor.gas_backend.exception.NotFoundException
import it.gasadvisor.gas_backend.model.GasStation
import it.gasadvisor.gas_backend.repository.GasPriceRepository
import it.gasadvisor.gas_backend.repository.GasStationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GasStationService @Autowired constructor(
    private val repository: GasStationRepository,
    private val priceRepository: GasPriceRepository
) {

    @Transactional
    fun saveAll(stations: List<GasStation>) {
        repository.saveAll(stations)
    }

    fun findAllLocations(): List<GetAllStationsResponse> {
        return repository.findAllLocations()
    }

    fun findLatestPrices(stationId: Long): List<GetStationPriceResponse> {
        return priceRepository.findLatestPriceByStationId(stationId).orElse(emptyList())
    }

    fun getStationData(stationId: Long): GetStationDataResponse {
        return repository.getStationData(stationId).orElseThrow { NotFoundException("Station with given id not found") }
    }

}
