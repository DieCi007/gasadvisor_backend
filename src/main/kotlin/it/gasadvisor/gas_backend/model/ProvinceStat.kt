package it.gasadvisor.gas_backend.model

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
    var province: Province
) {
    constructor(date: Instant, province: Province) : this(null, date, emptyList(), province)
}
