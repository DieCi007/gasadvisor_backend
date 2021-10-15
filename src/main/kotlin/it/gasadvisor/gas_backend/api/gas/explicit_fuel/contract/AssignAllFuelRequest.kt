package it.gasadvisor.gas_backend.api.gas.explicit_fuel.contract

import it.gasadvisor.gas_backend.model.enums.CommonFuelType

data class AssignAllFuelRequest(
    val type: CommonFuelType?,
    val fuels: List<Long>
)
