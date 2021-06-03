package it.gasadvisor.gas_backend.model
import javax.persistence.*

@Entity
@Table(name = "privilege")
class Privilege(
    @Id
    @Enumerated(EnumType.STRING)
    @Column(length = 48)
    var name: PrivilegeName,

    @Column(nullable = false)
    var description: String?,

    @ManyToMany(mappedBy = "privileges")
    var roles: Set<Role> = emptySet()
)

enum class PrivilegeName {
    READ_ALL, WRITE_ALL
}
