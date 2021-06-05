package it.gasadvisor.gas_backend.model

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
    var prices: Set<GasPrice> = emptySet()

) {
    constructor(id: Long) : this(
        id, "", "", "", "", "",
        "", "", 0.0, 0.0
    )
}
