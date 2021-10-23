package it.gasadvisor.gas_backend.api.gas.price.contract

import it.gasadvisor.gas_backend.model.enums.CommonFuelType
import it.gasadvisor.gas_backend.repository.contract.IFlagPrice

data class FuelTypeFlagPrices(
    val fuelType: CommonFuelType,
    val cheapestFlag: IFlagPrice,
    val mostExpensiveFlag: IFlagPrice
)
