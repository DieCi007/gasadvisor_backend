package it.gasadvisor.gas_backend.model.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.*

@Entity
@Table(name = "gas_station")
@JsonIgnoreProperties("prices")
class GasStation(
    @Id
    var id: Long,
    var owner: String,
    var flag: String,
    var type: String,
    var name: String,
    var address: String,
    var municipality: String,
    var province: String,
    @Column(nullable = false)
    var latitude: Double,
    @Column(nullable = false)
    var longitude: Double,
    @Enumerated(EnumType.STRING)
    var status: GasStationStatus,

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id.gasStation")
    @JsonIgnoreProperties(value = ["gasStation"], allowSetters = true)
    var prices: Set<GasPrice> = emptySet(),

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "station")
    @JsonIgnore
    var modifiedGasStation: ModifiedGasStation? = null

) {
    constructor(id: Long) : this(
        id, "", "", "", "", "",
        "", "", 0.0, 0.0, GasStationStatus.ACTIVE
    )

    constructor(id: Long, province: String) : this(
        id, "", "", "", "", "",
        "", province, 0.0, 0.0, GasStationStatus.ACTIVE
    )

    constructor(id: Long, province: String, municipality: String) : this(
        id, "", "", "", "", "",
        municipality, province, 0.0, 0.0, GasStationStatus.ACTIVE
    )
}

enum class GasStationStatus {
    ACTIVE, INACTIVE
}
