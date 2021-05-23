package it.gasadvisor.gas_backend.model
import javax.persistence.*

@Entity
@Table(name = "role")
class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    var name: RoleName,

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
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
