package it.gasadvisor.gas_backend.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

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
    var longitude: Double
)
