package it.gasadvisor.gas_backend.api.gas.explicit_fuel.contract

import it.gasadvisor.gas_backend.model.enums.CommonFuelType

interface IExplicitFuelType {
    fun getId(): Long?
    fun getName(): String
    fun getType(): CommonFuelType?
}
