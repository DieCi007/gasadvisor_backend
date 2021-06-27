package it.gasadvisor.gas_backend.model

import javax.persistence.*

@Entity
@Table(name = "municipality")
class Municipality(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,

    @Column(nullable = false, unique = true)
    var name: String,

    @ManyToOne(fetch = FetchType.EAGER)
    var province: Province
)
