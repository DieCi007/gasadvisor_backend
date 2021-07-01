package it.gasadvisor.gas_backend.model

import javax.persistence.*

@Entity
@Table(name = "price_stat")
data class PriceStat(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id: Long?,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private var fuelType: CommonFuelType,

    @Column(nullable = false)
    private var price: Double,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private var type: PriceStatType,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = true)
    private var gasStat: GasStat

)

enum class PriceStatType {
    AVG,
    LOW,
    HIGH
}
