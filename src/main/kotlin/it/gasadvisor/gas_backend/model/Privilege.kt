package it.gasadvisor.gas_backend.model

import javax.persistence.*

@Entity
@Table(name = "privilege")
data class Privilege(

    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    var name: PrivilegeName,

    @Column(nullable = false)
    var description: String,

    @ManyToMany(mappedBy = "privileges")
    var roles: Set<Role> = HashSet(0)
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0

}

enum class PrivilegeName {
    READ_ALL, WRITE_ALL
}
