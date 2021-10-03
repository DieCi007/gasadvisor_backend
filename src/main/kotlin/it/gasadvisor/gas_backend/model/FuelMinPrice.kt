package it.gasadvisor.gas_backend.model

import javax.persistence.*

@Entity
@Table(name = "fuel_min_price")
data class FuelMinPrice(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,

    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    var type: CommonFuelType,

    @Column(nullable = false)
    var minPrice: Double
)
