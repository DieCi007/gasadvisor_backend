package it.gasadvisor.gas_backend.model
import javax.persistence.*

@Entity
@Table(name = "role")
data class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    var name: RoleName,

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "role_has_privilege",
        joinColumns = [JoinColumn(name = "role_id", nullable = false)],
        inverseJoinColumns = [JoinColumn(name = "privilege_id", nullable = false)]
    )
    var privileges: Set<Privilege>,

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "role")
    var users: Set<User>
)

enum class RoleName {
    ADMIN,
    END_USER,
    GUEST,
    UNDEFINED
}
