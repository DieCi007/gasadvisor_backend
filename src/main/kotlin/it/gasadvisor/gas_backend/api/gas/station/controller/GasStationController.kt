package it.gasadvisor.gas_backend.api.gas.station.controller

import it.gasadvisor.gas_backend.api.gas.station.contract.*
import it.gasadvisor.gas_backend.api.gas.station.service.GasStationService
import it.gasadvisor.gas_backend.api.gas.station.service.SortType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = ["/api/v1/gas/station"], produces = [MediaType.APPLICATION_JSON_VALUE])
class GasStationController @Autowired constructor(
    private val service: GasStationService
) {

    @GetMapping("/analytics")
    @PreAuthorize("hasAuthority(\"READ_ALL\")")
    fun adminGetAll(
        @RequestParam(required = false) page: Int?,
        @RequestParam(required = false) size: Int?,
        @RequestParam(required = false) sortBy: String?,
        @RequestParam(required = false) sortType: SortType?
    ): PaginatedResponse<GasStationResponse> {
        return service.findAllPaginated(page, size, sortBy, sortType)
    }

    @GetMapping
    fun findAll(): List<GetAllStationsResponse> {
        return service.findAllLocations()
    }

    @GetMapping("/{stationId}/today")
    fun findLatestPrices(@PathVariable("stationId") stationId: Long): List<GetStationPriceResponse> {
        return service.findLatestPrices(stationId)
    }

    @GetMapping("/{stationId}")
    fun getStationData(@PathVariable("stationId") stationId: Long): GetStationDataResponse {
        return service.getStationData(stationId)
    }
}
