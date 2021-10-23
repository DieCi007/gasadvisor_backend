package it.gasadvisor.gas_backend.model.entities

import javax.persistence.*

@Entity
@Table(name = "province")
class Province(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,

    @Column(nullable = false, unique = true)
    var name: String,

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "province", targetEntity = Municipality::class)
    var municipalities: Set<Municipality>? = emptySet(),

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "province", targetEntity = ProvinceStat::class)
    var stats: List<ProvinceStat>? = emptyList()
) {
    constructor(name: String) : this(null, name)
}
