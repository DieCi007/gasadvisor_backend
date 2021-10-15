package it.gasadvisor.gas_backend.api.gas.unresolved_station.contract

import javax.validation.constraints.NotNull

class ChangeUnresolvedStationRequest (
    @NotNull
    val isResolved: Boolean
)
