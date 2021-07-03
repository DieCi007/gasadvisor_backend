package it.gasadvisor.gas_backend.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.*

@Entity
@Table(name = "gas_station")
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id.gasStation")
    @JsonIgnoreProperties(value = ["gasStation"], allowSetters = true)
    var prices: Set<GasPrice> = emptySet()

) {
    constructor(id: Long) : this(
        id, "", "", "", "", "",
        "", "", 0.0, 0.0
    )

    constructor(id: Long, province: String) : this(
        id, "", "", "", "", "",
        "", province, 0.0, 0.0
    )

    constructor(id: Long, province: String, municipality: String) : this(
        id, "", "", "", "", "",
        municipality, province, 0.0, 0.0
    )
}
