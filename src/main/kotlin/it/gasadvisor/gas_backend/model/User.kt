package it.gasadvisor.gas_backend.model

import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Entity
@Table(name = "user")
class User(
    @Id
    @Column(length = 20)
    var username: String,

    @NotBlank
    @Size(min = 6)
    @Column(nullable = false)
    var password: String,

    @NotBlank
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    var role: Role

)
