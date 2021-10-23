package it.gasadvisor.gas_backend.model.entities
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

    @ManyToMany(mappedBy = "privileges", targetEntity = Role::class)
    var roles: Set<Role> = emptySet()
)

enum class PrivilegeName {
    READ_ALL, WRITE_ALL
}
