package it.gasadvisor.gas_backend.model.entities

import it.gasadvisor.gas_backend.model.enums.PriceStatType
import it.gasadvisor.gas_backend.model.enums.CommonFuelType
import javax.persistence.*

@Entity
@Table(name = "price_stat")
class PriceStat(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var fuelType: CommonFuelType,

    @Column(nullable = true)
    var price: Double?,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var priceStatType: PriceStatType,

    @ManyToOne(fetch = FetchType.LAZY, optional = true, targetEntity = GasStat::class)
    @JoinColumn(name = "gas_stat_id", referencedColumnName = "id")
    var gasStat: GasStat?,

    @ManyToOne(fetch = FetchType.LAZY, optional = true, targetEntity = ProvinceStat::class)
    var provinceStat: ProvinceStat?
) {
    constructor(type: CommonFuelType, price: Double?, priceStatType: PriceStatType) :
            this(null, type, price, priceStatType, null, null)

    constructor(type: CommonFuelType, price: Double?, priceStatType: PriceStatType, gasStat: GasStat) :
            this(null, type, price, priceStatType, gasStat, null)

    override fun toString(): String {
        return "$id $fuelType $priceStatType"
    }
}


