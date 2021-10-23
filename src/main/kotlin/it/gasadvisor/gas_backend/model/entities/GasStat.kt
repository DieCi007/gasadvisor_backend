package it.gasadvisor.gas_backend.model.entities

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.Instant
import javax.persistence.*

@Entity
@Table(name = "gas_stat")
@JsonIgnoreProperties("id")
class GasStat(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,

    @Column(nullable = false, unique = true)
    var date: Instant,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = true)
    var mostStationsProvince: Province?,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = true)
    var mostStationsMunicipality: Municipality?,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = true)
    var leastStationsProvince: Province?,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = true)
    var leastStationsMunicipality: Municipality?,

    @OneToMany(
        fetch = FetchType.EAGER,
        mappedBy = "gasStat", targetEntity = PriceStat::class
    )
    var prices: List<PriceStat>
) {

    constructor(date: Instant) : this(
        null, date, null, null, null, null,
        emptyList()
    )

    override fun toString(): String {
        return "$id $date ${prices.map { it.toString() }}"
    }
}
