package it.gasadvisor.gas_backend.api.gas.unresolved_station.contract

import it.gasadvisor.gas_backend.model.entities.UnresolvedGasStation

data class GetAllUnresolvedResponse(
    val id: Long?,
    val value: String,
    val isResolved: Boolean
) {
    companion object {
        fun fromUnresolvedStation(s: UnresolvedGasStation): GetAllUnresolvedResponse {
            return GetAllUnresolvedResponse(s.id, s.value, s.isResolved)
        }
    }
}
