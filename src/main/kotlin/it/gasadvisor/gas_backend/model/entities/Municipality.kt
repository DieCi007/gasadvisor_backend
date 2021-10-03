package it.gasadvisor.gas_backend.model.entities

import javax.persistence.*

@Entity
@Table(
    name = "municipality", uniqueConstraints = [
        UniqueConstraint(columnNames = ["name", "province_id"])
    ]
)
class Municipality(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,

    @Column(nullable = false)
    var name: String,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "province_id")
    var province: Province
)
