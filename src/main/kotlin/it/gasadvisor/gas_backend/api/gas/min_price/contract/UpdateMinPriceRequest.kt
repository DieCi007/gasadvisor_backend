package it.gasadvisor.gas_backend.api.gas.min_price.contract

import it.gasadvisor.gas_backend.model.CommonFuelType

data class UpdateMinPriceRequest(
    val type: CommonFuelType,
    val minPrice: Double
)
