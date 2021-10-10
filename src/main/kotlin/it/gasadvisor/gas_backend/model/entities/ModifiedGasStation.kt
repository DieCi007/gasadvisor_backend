package it.gasadvisor.gas_backend.model.entities

import it.gasadvisor.gas_backend.api.gas.station.contract.UpdateGasStationRequest
import javax.persistence.*

@Entity
@Table(name = "modified_gas_station")
class ModifiedGasStation(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "station_id", nullable = false)
    var station: GasStation,

    @Enumerated(EnumType.STRING)
    var status: GasStationStatus?,

    var owner: String? = null,
    var flag: String? = null,
    var type: String? = null,
    var name: String? = null,
    var address: String? = null,
    var municipality: String? = null,
    var province: String? = null,
    var latitude: Double? = null,
    var longitude: Double? = null,

    ) {
    constructor(station: GasStation, status: GasStationStatus) : this(
        null, station, status
    )

    fun update(request: UpdateGasStationRequest, saved: GasStation): ModifiedGasStation {
        owner = if ((owner != null && request.owner != owner) ||
            (owner == null && request.owner != saved.owner)
        ) request.owner else null

        flag = if ((flag != null && request.flag != flag) ||
            (flag == null && request.flag != saved.flag)
        ) request.flag else null

        type = if ((type != null && request.type != type) ||
            (type == null && request.type != saved.type)
        ) request.type else null

        name = if ((name != null && request.name != name) ||
            (name == null && request.name != saved.name)
        ) request.name else null

        address = if ((address != null && request.address != address) ||
            (address == null && request.address != saved.address)
        ) request.address else null

        municipality = if ((municipality != null && request.municipality != municipality) ||
            (municipality == null && request.municipality != saved.municipality)
        ) request.municipality else null

        province = if ((province != null && request.province != province) ||
            (province == null && request.province != saved.province)
        ) request.province else null

        latitude = if ((latitude != null && request.latitude != latitude) ||
            (latitude == null && request.latitude != saved.latitude)
        ) request.latitude else null

        longitude = if ((longitude != null && request.longitude != longitude) ||
            (longitude == null && request.longitude != saved.longitude)
        ) request.longitude else null

        status = request.status
        return this
    }

    companion object {
        fun fromUpdateRequest(request: UpdateGasStationRequest, saved: GasStation): ModifiedGasStation {
            val owner = if (request.owner == saved.owner) null else request.owner
            val flag = if (request.flag == saved.flag) null else request.flag
            val type = if (request.type == saved.type) null else request.type
            val name = if (request.name == saved.name) null else request.name
            val address = if (request.address == saved.address) null else request.address
            val municipality = if (request.municipality == saved.municipality) null else request.municipality
            val province = if (request.province == saved.province) null else request.province
            val latitude = if (request.latitude == saved.latitude) null else request.latitude
            val longitude = if (request.longitude == saved.longitude) null else request.longitude
            return ModifiedGasStation(
                null, saved, request.status, owner,
                flag, type, name, address, municipality, province, latitude, longitude
            )
        }
    }
}

