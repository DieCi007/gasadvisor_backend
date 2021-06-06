package it.gasadvisor.gas_backend.api.gas.station.contract

import java.time.Instant

data class GetStationPriceResponse(
    var price: Double,
    var isSelf: Boolean,
    var readDate: Instant,
    var description: String
)
