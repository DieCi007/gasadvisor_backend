package it.gasadvisor.gas_backend.api.gas.price.controller

import it.gasadvisor.gas_backend.api.gas.price.contract.AppStatsResponse
import it.gasadvisor.gas_backend.api.gas.price.contract.FuelTypeFlagPrices
import it.gasadvisor.gas_backend.api.gas.price.contract.PriceAnalyticsResponse
import it.gasadvisor.gas_backend.api.gas.price.service.GasPriceService
import it.gasadvisor.gas_backend.api.gas.station.contract.PaginatedResponse
import it.gasadvisor.gas_backend.model.enums.CommonFuelType
import it.gasadvisor.gas_backend.model.enums.PriceStatType
import it.gasadvisor.gas_backend.model.enums.SortType
import it.gasadvisor.gas_backend.repository.contract.IDatePrice
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.time.Instant

@RestController
@RequestMapping(value = ["/api/v1/gas/price"], produces = [MediaType.APPLICATION_JSON_VALUE])
class PriceController @Autowired constructor(
    private val service: GasPriceService
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
    ): PaginatedResponse<PriceAnalyticsResponse> {
        return service.findAllPaginated(page, size, sortBy, sortType, query)
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority(\"WRITE_ALL\")")
    @ResponseStatus(HttpStatus.OK)
    fun delete(
        @RequestParam isSelf: Boolean, @RequestParam readDate: Instant,
        @RequestParam description: String, @RequestParam price: Double,
        @RequestParam stationId: Long
    ) {
        return service.delete(isSelf, readDate, description, price, stationId)
    }

    @GetMapping("/stats/fuel-flag")
    @ResponseStatus(HttpStatus.OK)
    fun getStatsForToday(): List<FuelTypeFlagPrices> {
        return service.priceStatsForFlag
    }

    @GetMapping("/stats/price-trend")
    @ResponseStatus(HttpStatus.OK)
    fun getPriceTrend(
        @RequestParam("fuelType") fuelType: CommonFuelType,
        @RequestParam("statType") statType: PriceStatType
    ): List<IDatePrice> {
        return service.getPriceTrend(fuelType, statType)
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    fun getAppStats(
        @RequestParam(value = "province", required = false) province: String?,
        @RequestParam(value = "date") date: Instant,
    ): AppStatsResponse {
        return service.getAppStats(province, date)
    }
}
