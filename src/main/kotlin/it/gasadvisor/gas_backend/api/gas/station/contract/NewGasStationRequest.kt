package it.gasadvisor.gas_backend.api.gas.station.contract

import it.gasadvisor.gas_backend.model.entities.GasStation
import it.gasadvisor.gas_backend.model.entities.GasStationStatus
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class NewGasStationRequest(
    @NotNull
    var id: Long,
    @NotBlank
    var owner: String,
    @NotBlank
    var flag: String,
    @NotBlank
    var type: String,
    @NotBlank
    var name: String,
    @NotBlank
    var address: String,
    @NotBlank
    var municipality: String,
    @NotBlank
    var province: String,
    @NotNull
    var latitude: Double,
    @NotNull
    var longitude: Double,
    @NotBlank
    var status: GasStationStatus,
) {
    fun toGasStation(): GasStation {
        return GasStation(
            id, owner, flag, type, name, address, municipality,
            province, latitude, longitude, status
        )
    }
}
