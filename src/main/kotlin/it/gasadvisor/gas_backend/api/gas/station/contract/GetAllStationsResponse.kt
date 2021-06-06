package it.gasadvisor.gas_backend.api.gas.station.contract

data class GetAllStationsResponse(
    val id: Long,
    var latitude: Double,
    var longitude: Double,
)
