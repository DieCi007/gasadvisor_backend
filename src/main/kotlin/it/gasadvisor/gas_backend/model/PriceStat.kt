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

    @Column(nullable = true)
    private var price: Double?,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private var priceStatType: PriceStatType,

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    private var gasStat: GasStat?,

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    private var provinceStat: ProvinceStat?
) {
    constructor(type: CommonFuelType, price: Double?, priceStatType: PriceStatType) :
            this(null, type, price, priceStatType, null, null)
}


