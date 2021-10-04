package it.gasadvisor.gas_backend.api.gas.station.contract

import it.gasadvisor.gas_backend.model.entities.GasStation

data class GasStationAnalyticsResponse(
    var id: Long?,
    var owner: String?,
    var flag: String?,
    var type: String?,
    var name: String?,
    var address: String?,
    var municipality: String?,
    var province: String?,
    var latitude: Double?,
    var longitude: Double?
) {
    companion object {
        fun fromGasStation(station: GasStation): GasStationAnalyticsResponse {
            return GasStationAnalyticsResponse(
                station.id, station.owner, station.flag,
                station.type, station.name, station.address,
                station.municipality, station.province,
                station.latitude, station.longitude
            )
        }
    }
}
