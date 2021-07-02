package it.gasadvisor.gas_backend.model

import javax.persistence.*

@Entity
@Table(name = "explicit_fuel_type")
data class ExplicitFuelType(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,

    @Column(nullable = false, unique = true)
    var name: String,

    @Enumerated(EnumType.STRING)
    var commonType: CommonFuelType?
) {
    constructor(name: String) : this(null, name, null) {
    }
}
