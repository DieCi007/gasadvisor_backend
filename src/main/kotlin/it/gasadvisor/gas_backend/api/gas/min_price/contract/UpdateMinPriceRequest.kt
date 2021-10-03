package it.gasadvisor.gas_backend.api.gas.min_price.contract

import it.gasadvisor.gas_backend.model.enums.CommonFuelType
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class UpdateMinPriceRequest(
    @NotBlank
    @NotEmpty
    val type: CommonFuelType,
    @NotNull
    val minPrice: Double
)
