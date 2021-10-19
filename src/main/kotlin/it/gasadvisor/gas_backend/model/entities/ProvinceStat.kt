package it.gasadvisor.gas_backend.model.entities

import java.time.Instant
import javax.persistence.*

@Entity
@Table(name = "province_stat")
data class ProvinceStat(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id: Long?,

    @Column(nullable = false)
    var date: Instant,

    @OneToMany(
        fetch = FetchType.EAGER, cascade = [CascadeType.ALL],
        mappedBy = "provinceStat"
    )
    var prices: List<PriceStat> = emptyList(),

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = true)
    var mostStationsMunicipality: Municipality?,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = true)
    var leastStationsMunicipality: Municipality?,

    @ManyToOne(fetch = FetchType.EAGER)
    var province: Province
) {
    constructor(date: Instant, province: Province) : this(null, date, emptyList(), null, null, province)
    constructor(
        date: Instant, mostStationsMunicipality: Municipality,
        leastStationsMunicipality: Municipality, province: Province
    ) :
            this(null, date, emptyList(), mostStationsMunicipality, leastStationsMunicipality, province)
}
