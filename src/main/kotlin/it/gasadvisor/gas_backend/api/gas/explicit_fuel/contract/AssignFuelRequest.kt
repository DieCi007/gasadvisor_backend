package it.gasadvisor.gas_backend.api.gas.explicit_fuel.contract

import it.gasadvisor.gas_backend.model.enums.CommonFuelType
import javax.validation.constraints.NotBlank

data class AssignFuelRequest(
    @NotBlank
    val type: CommonFuelType
)
