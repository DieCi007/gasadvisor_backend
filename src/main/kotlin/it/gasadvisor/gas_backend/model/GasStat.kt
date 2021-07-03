package it.gasadvisor.gas_backend.model

import java.time.Instant
import javax.persistence.*

@Entity
@Table(name = "gas_stat")
data class GasStat(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id: Long?,

    @Column(nullable = false)
    private var date: Instant,

    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    private var prices: List<PriceStat>,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = true)
    private var mostStationsProvince: Province?,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = true)
    private var mostStationsMunicipality: Municipality?,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = true)
    private var leastStationsProvince: Province?,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = true)
    private var leastStationsMunicipality: Municipality?
)
