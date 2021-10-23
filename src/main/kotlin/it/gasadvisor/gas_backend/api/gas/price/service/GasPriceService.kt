package it.gasadvisor.gas_backend.api.gas.price.service

import it.gasadvisor.gas_backend.api.gas.price.contract.FuelTypeFlagPrices
import it.gasadvisor.gas_backend.api.gas.price.contract.PriceAnalyticsResponse
import it.gasadvisor.gas_backend.api.gas.station.contract.PaginatedResponse
import it.gasadvisor.gas_backend.model.entities.GasPrice
import it.gasadvisor.gas_backend.model.entities.GasPriceId
import it.gasadvisor.gas_backend.model.entities.GasStation
import it.gasadvisor.gas_backend.model.enums.CommonFuelType
import it.gasadvisor.gas_backend.model.enums.PriceStatType
import it.gasadvisor.gas_backend.model.enums.SortType
import it.gasadvisor.gas_backend.repository.GasPriceRepository
import it.gasadvisor.gas_backend.repository.PriceStatRepository
import it.gasadvisor.gas_backend.repository.contract.IDatePrice
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.transaction.Transactional

@Service
class GasPriceService @Autowired constructor(
    private val repository: GasPriceRepository,
    private val priceStatRepo: PriceStatRepository
) {

    @Transactional
    fun saveAll(prices: List<GasPrice>) {
        repository.saveAll(prices)
    }

    fun findAllPaginated(
        page: Int?,
        size: Int?,
        sortBy: String?,
        sortType: SortType?,
        query: String?
    ): PaginatedResponse<PriceAnalyticsResponse> {
        var sort = when (sortBy) {
            null -> Sort.by("id.readDate")
            "readDate" -> Sort.by("id.readDate")
            "isSelf" -> Sort.by("id.isSelf")
            "description" -> Sort.by("id.description")
            "stationId" -> Sort.by("id.gasStation.id")
            else -> Sort.by(sortBy)
        }

        sort = if (sortType == null || sortType == SortType.ASC) sort.ascending() else sort.descending()
        val pageRequest =
            if (page == null || size == null) PageRequest.of(0, 10, sort) else PageRequest.of(page, size, sort)
        val searchBy = query ?: ""
        val spec = PriceSpecification.filter(searchBy)
        val result = repository.findAll(spec, pageRequest)
        return PaginatedResponse(
            pageRequest.pageNumber, pageRequest.pageSize,
            result.totalElements, result.totalPages,
            result.content.map { PriceAnalyticsResponse.fromPrice(it) }
        )
    }

    fun delete(
        isSelf: Boolean, readDate: Instant,
        description: String, price: Double,
        stationId: Long
    ) {
        val id = GasPriceId(
            GasStation(stationId), isSelf,
            readDate, description
        )
        repository.deleteById(id)
    }

    fun getMinMaxPricesForForFlag(): List<FuelTypeFlagPrices> {
        val fuelTypeFlagPrices: MutableList<FuelTypeFlagPrices> = mutableListOf()
        CommonFuelType.values().forEach { t ->
            val avgPrices = repository.findAvgPriceForFlags(t)
            if (avgPrices.isNotEmpty()) {
                val cheapest = avgPrices.first()
                val expensive = avgPrices.last()
                val fuelPrices = FuelTypeFlagPrices(
                    t, cheapest, expensive
                )
                fuelTypeFlagPrices.add(fuelPrices)
            }
        }
        return fuelTypeFlagPrices
    }

    fun getPriceTrend(fuelType: CommonFuelType, statType: PriceStatType): List<IDatePrice> {
        return priceStatRepo.findPriceTrend(Instant.now().minus(30, ChronoUnit.DAYS),
        fuelType, statType)
    }
}
