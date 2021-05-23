package it.gasadvisor.gas_backend.model
import javax.persistence.*

@Entity
@Table(name = "privilege")
class Privilege(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    var name: PrivilegeName,

    @Column(nullable = false)
    var description: String,

    @ManyToMany
    @JoinTable(
        name = "role_has_privilege",
        joinColumns = [JoinColumn(name = "privilege_id", nullable = false)],
        inverseJoinColumns = [JoinColumn(name = "role_id", nullable = false)]
    )
    var roles: Set<Role>
)

enum class PrivilegeName {
    READ_ALL, WRITE_ALL
}
