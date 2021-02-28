package it.gasadvisor.gas_backend.model

import it.gasadvisor.gas_backend.api.auth.domain.UserRole
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Entity
@Table
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(unique = true)
    var username: String,

    @NotBlank
    @Size(min = 6)
    @Column
    var password: String,

    @Enumerated(EnumType.STRING)
    var role: UserRole
)
