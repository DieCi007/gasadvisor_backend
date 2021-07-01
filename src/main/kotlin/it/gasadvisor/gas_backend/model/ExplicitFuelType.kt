package it.gasadvisor.gas_backend.model

import javax.persistence.*

@Entity
@Table(name = "explicit_fuel_type")
data class ExplicitFuelType(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id: Long?,

    @Column(nullable = false, unique = true)
    private var name: String,

    @Enumerated(EnumType.STRING)
    private var commonType: CommonFuelType

)
