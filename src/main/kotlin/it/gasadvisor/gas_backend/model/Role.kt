package it.gasadvisor.gas_backend.model
import javax.persistence.*

@Entity
@Table(name = "role")
class Role(
    @Id
    @Enumerated(EnumType.STRING)
    @Column(length = 48)
    var name: RoleName,

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "role_has_privilege",
        joinColumns = [JoinColumn(name = "role_id", nullable = false)],
        inverseJoinColumns = [JoinColumn(name = "privilege_id", nullable = false)]
    )
    var privileges: Set<Privilege> = emptySet(),

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "role")
    var users: Set<User> = emptySet()
)

enum class RoleName {
    ADMIN,
    END_USER,
    GUEST,
    UNDEFINED
}
