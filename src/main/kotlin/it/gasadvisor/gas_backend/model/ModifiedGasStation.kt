package it.gasadvisor.gas_backend.model

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
    var status: GasStationStatus,

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
}

