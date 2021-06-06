package it.gasadvisor.gas_backend.api.gas.station.controller

import it.gasadvisor.gas_backend.api.gas.station.contract.GetAllStationsResponse
import it.gasadvisor.gas_backend.api.gas.station.contract.GetStationDataResponse
import it.gasadvisor.gas_backend.api.gas.station.contract.GetStationPriceResponse
import it.gasadvisor.gas_backend.api.gas.station.service.GasStationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/api/v1/gas/station"], produces = [MediaType.APPLICATION_JSON_VALUE])
class GasStationController @Autowired constructor(
    private val service: GasStationService
) {

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
