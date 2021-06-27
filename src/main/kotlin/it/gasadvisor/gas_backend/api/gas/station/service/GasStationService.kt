package it.gasadvisor.gas_backend.api.gas.station.service

import it.gasadvisor.gas_backend.api.gas.station.contract.*
import it.gasadvisor.gas_backend.exception.NotFoundException
import it.gasadvisor.gas_backend.model.GasStation
import it.gasadvisor.gas_backend.repository.GasPriceRepository
import it.gasadvisor.gas_backend.repository.GasStationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
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
        return priceRepository.findLatestPriceByStationId(stationId)
    }

    fun getStationData(stationId: Long): GetStationDataResponse {
        return repository.getStationData(stationId).orElseThrow { NotFoundException("Station with given id not found") }
    }

    fun findAllPaginated(page: Int?, size: Int?, sortBy: String?, sortType: SortType?): PaginatedResponse<GasStationAnalyticsResponse> {
        var sort = if (sortBy == null) Sort.by("id") else Sort.by(sortBy)
        sort = if (sortType == null || sortType == SortType.ASC) sort.ascending() else sort.descending()
        val pageRequest =
            if (page == null || size == null) PageRequest.of(0, 10, sort) else PageRequest.of(page, size, sort)
        val result = repository.findAllPaginated(pageRequest)
        return PaginatedResponse(pageRequest.pageNumber, pageRequest.pageSize,
        result.totalElements, result.totalPages, result.content)
    }
}

enum class SortType {
    ASC, DESC
}