package it.gasadvisor.gas_backend.model.entities

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.Instant
import javax.persistence.*

@Entity
@Table(name = "gas_stat")
@JsonIgnoreProperties("id")
data class GasStat(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,

    @Column(nullable = false)
    var date: Instant,

    @OneToMany(
        fetch = FetchType.EAGER, cascade = [CascadeType.ALL],
        mappedBy = "gasStat"
    )
    var prices: List<PriceStat>,

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
    var leastStationsMunicipality: Municipality?
) {
    constructor(date: Instant) : this(
        null, date, emptyList(), null, null, null, null
    )
}
