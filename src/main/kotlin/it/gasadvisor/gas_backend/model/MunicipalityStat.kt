package it.gasadvisor.gas_backend.model

import java.time.Instant
import javax.persistence.*

@Entity
@Table(name = "municipality_stat")
data class MunicipalityStat(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id: Long?,

    @Column(nullable = false)
    var date: Instant,

    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    var prices: List<PriceStat>,

    @ManyToOne(fetch = FetchType.EAGER)
    var municipality: Municipality
)
