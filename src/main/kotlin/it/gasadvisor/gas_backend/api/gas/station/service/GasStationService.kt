package it.gasadvisor.gas_backend.api.gas.station.service

import it.gasadvisor.gas_backend.api.gas.station.contract.*
import it.gasadvisor.gas_backend.exception.BadRequestException
import it.gasadvisor.gas_backend.exception.NotFoundException
import it.gasadvisor.gas_backend.model.entities.GasStation
import it.gasadvisor.gas_backend.model.entities.ModifiedGasStation
import it.gasadvisor.gas_backend.model.enums.SortType
import it.gasadvisor.gas_backend.repository.GasPriceRepository
import it.gasadvisor.gas_backend.repository.GasStationRepository
import it.gasadvisor.gas_backend.repository.ModifiedGasStationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GasStationService @Autowired constructor(
    private val repository: GasStationRepository,
    private val modifiedStationRepository: ModifiedGasStationRepository,
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

    fun findAllPaginated(
        page: Int?,
        size: Int?,
        sortBy: String?,
        sortType: SortType?,
        query: String?
    ): PaginatedResponse<GasStationAnalyticsResponse> {
        var sort = if (sortBy == null) Sort.by("id") else Sort.by(sortBy)
        sort = if (sortType == null || sortType == SortType.ASC) sort.ascending() else sort.descending()
        val pageRequest =
            if (page == null || size == null) PageRequest.of(0, 10, sort) else PageRequest.of(page, size, sort)
        val searchBy = query ?: ""
        val spec = StationSpecification.filter(searchBy)
        val result = repository.findAll(spec, pageRequest)
        return PaginatedResponse(
            pageRequest.pageNumber, pageRequest.pageSize,
            result.totalElements, result.totalPages,
            result.content.map { GasStationAnalyticsResponse.fromGasStation(it) }
        )
    }

    @Transactional
    fun adminFindById(id: Long): GasStation {
        return repository.findById(id).orElseThrow { NotFoundException("Station not found") }
    }

    fun create(station: UpdateGasStationRequest) {
        if (repository.findById(station.id).isPresent) {
            throw BadRequestException("This station already exists. Please update it.")
        }
        val newStation = station.toGasStation()
        repository.save(newStation)
    }

    fun update(station: UpdateGasStationRequest): GasStationAnalyticsResponse {
        val saved = repository.findById(station.id).orElseThrow { NotFoundException("Gas station not found") }
        val modifiedStation = modifiedStationRepository.findByStationId(station.id).map { it.update(station, saved) }
            .orElse(ModifiedGasStation.fromUpdateRequest(station, saved))
        modifiedStationRepository.save(modifiedStation)
        saved.owner = station.owner
        saved.flag = station.flag
        saved.type = station.type
        saved.name = station.name
        saved.address = station.address
        saved.municipality = station.municipality
        saved.province = station.province
        saved.latitude = station.latitude
        saved.longitude = station.longitude
        saved.status = station.status
        return GasStationAnalyticsResponse.fromGasStation(repository.save(saved))
    }

    fun getLocationNoStations(): LocationNoStations {
        val provinceMostStations = repository.findProvinceWithMostStations()
        val provinceLeastStations = repository.findProvinceWithLeastStations()
        val municipalityMostStations = repository.findMunicipalityWithMostStations()
        val municipalityLeastStations = repository.findMunicipalityWithLeastStations()

        return LocationNoStations(
            provinceMostStations, provinceLeastStations,
            municipalityMostStations, municipalityLeastStations
        )

    }


}
