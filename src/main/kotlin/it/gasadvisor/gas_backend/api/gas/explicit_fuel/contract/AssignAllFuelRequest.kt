package it.gasadvisor.gas_backend.api.gas.explicit_fuel.contract

import it.gasadvisor.gas_backend.model.enums.CommonFuelType
import javax.validation.constraints.NotNull

data class AssignAllFuelRequest(
    @NotNull
    val type: CommonFuelType,
    val fuels: List<Long>
)
