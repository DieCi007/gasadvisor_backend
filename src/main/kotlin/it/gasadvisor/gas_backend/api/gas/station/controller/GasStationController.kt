package it.gasadvisor.gas_backend.api.gas.station.controller

import it.gasadvisor.gas_backend.api.gas.station.contract.*
import it.gasadvisor.gas_backend.api.gas.station.service.GasStationService
import it.gasadvisor.gas_backend.model.entities.GasStation
import it.gasadvisor.gas_backend.model.enums.CommonFuelType
import it.gasadvisor.gas_backend.model.enums.SortType
import it.gasadvisor.gas_backend.repository.contract.INearestStation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping(value = ["/api/v1/gas/station"], produces = [MediaType.APPLICATION_JSON_VALUE])
class GasStationController @Autowired constructor(
    private val service: GasStationService
) {

    @GetMapping("/analytics")
    @PreAuthorize("hasAuthority(\"READ_ALL\")")
    @ResponseStatus(HttpStatus.OK)
    fun adminGetAll(
        @RequestParam(required = false) page: Int?,
        @RequestParam(required = false) size: Int?,
        @RequestParam(required = false) sortBy: String?,
        @RequestParam(required = false) sortType: SortType?,
        @RequestParam(required = false) query: String?,
    ): PaginatedResponse<GasStationAnalyticsResponse> {
        return service.findAllPaginated(page, size, sortBy, sortType, query)
    }

    @GetMapping("/analytics/{id}")
    @PreAuthorize("hasAuthority(\"READ_ALL\")")
    @ResponseStatus(HttpStatus.OK)
    fun adminGetById(@PathVariable("id") id: Long): GasStation {
        return service.adminFindById(id)
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun findAll(): List<GetAllStationsResponse> {
        return service.findAllLocations()
    }

    @PostMapping
    @PreAuthorize("hasAuthority(\"WRITE_ALL\")")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody @Valid station: UpdateGasStationRequest) {
        return service.create(station)
    }

    @PatchMapping
    @PreAuthorize("hasAuthority(\"WRITE_ALL\")")
    @ResponseStatus(HttpStatus.OK)
    fun update(@RequestBody @Valid station: UpdateGasStationRequest): GasStationAnalyticsResponse {
        return service.update(station)
    }

    @GetMapping("/{stationId}/today")
    @ResponseStatus(HttpStatus.OK)
    fun findLatestPrices(@PathVariable("stationId") stationId: Long): List<GetStationPriceResponse> {
        return service.findLatestPrices(stationId)
    }

    @GetMapping("/{stationId}")
    @ResponseStatus(HttpStatus.OK)
    fun getStationData(@PathVariable("stationId") stationId: Long): GetStationDataResponse {
        return service.getStationData(stationId)
    }

    @GetMapping("/stats/location-stations")
    @ResponseStatus(HttpStatus.OK)
    fun getStatsForToday(): LocationNoStations {
        return service.getLocationNoStations()
    }

    @GetMapping("/near")
    @ResponseStatus(HttpStatus.OK)
    fun getNearest(
        @RequestParam("latitude") latitude: Double,
        @RequestParam("longitude") longitude: Double,
        @RequestParam(value = "size", required = false) size: Int?
    ): List<INearestStation> {
        return service.findNearestStations(latitude, longitude, size)
    }

    @GetMapping("/filter")
    @ResponseStatus(HttpStatus.OK)
    fun filter(
        @RequestParam(value = "province", required = false) province: String?,
        @RequestParam(value = "municipality", required = false) municipality: String?,
        @RequestParam(value = "fuel", required = false) fuel: CommonFuelType?,
        @RequestParam(value = "distance", required = false) distance: Long?,
        @RequestParam(value = "latitude", required = false) lat: Double?,
        @RequestParam(value = "longitude", required = false) lon: Double?
    ): List<IGetAllStationsResponse> {
        return service.filter(province, municipality, fuel, distance, lat, lon)
    }

}
