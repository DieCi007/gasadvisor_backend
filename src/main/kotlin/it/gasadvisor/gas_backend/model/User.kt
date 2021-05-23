package it.gasadvisor.gas_backend.model

import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Entity
@Table(name = "user")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(unique = true, nullable = false)
    var username: String,

    @NotBlank
    @Size(min = 6)
    @Column(nullable = false)
    var password: String,

    @NotBlank
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    var role: Role

)
