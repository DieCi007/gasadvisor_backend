package it.gasadvisor.gas_backend.api.gas.price.contract

import it.gasadvisor.gas_backend.model.entities.GasPrice
import java.time.Instant

data class PriceAnalyticsResponse(
    var isSelf: Boolean?,
    var readDate: Instant?,
    var description: String?,
    var price: Double,
    var stationId: Long?
) {
    companion object {
        fun fromPrice(price: GasPrice): PriceAnalyticsResponse {
            return PriceAnalyticsResponse(
                price.id.isSelf, price.id.readDate,
                price.id.description, price.price, price.id.gasStation.id
            )
        }
    }
}
