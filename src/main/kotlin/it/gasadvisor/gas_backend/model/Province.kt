package it.gasadvisor.gas_backend.model

import javax.persistence.*

@Entity
@Table(name = "province")
class Province(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,

    @Column(nullable = false, unique = true)
    var name: String,

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "province")
    var municipalities: Set<Municipality> = emptySet()
)
