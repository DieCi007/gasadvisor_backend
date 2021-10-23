package it.gasadvisor.gas_backend.api.gas.price.contract

import it.gasadvisor.gas_backend.model.enums.CommonFuelType
import it.gasadvisor.gas_backend.model.enums.PriceStatType

data class PriceStatResponse(
    val fuelType: CommonFuelType,
    val price: Double,
    val priceStatType: PriceStatType
)
