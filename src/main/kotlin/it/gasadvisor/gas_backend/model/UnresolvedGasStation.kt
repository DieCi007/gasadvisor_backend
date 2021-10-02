package it.gasadvisor.gas_backend.model

import javax.persistence.*

@Entity
@Table(name = "unresolved_gas_station")
class UnresolvedGasStation(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,
    var value: String,
    @Column(name = "isResolved")
    var isResolved: Boolean
) {
    constructor(value: String, resolved: Boolean) : this(null, value, resolved)
}
