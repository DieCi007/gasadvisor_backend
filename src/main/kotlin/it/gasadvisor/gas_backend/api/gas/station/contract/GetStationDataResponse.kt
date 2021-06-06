package it.gasadvisor.gas_backend.api.gas.station.contract

data class GetStationDataResponse(
    var owner: String,
    var flag: String,
    var type: String,
    var name: String,
    var address: String,
    var municipality: String,
    var province: String,
)
