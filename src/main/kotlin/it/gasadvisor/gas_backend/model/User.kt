package it.gasadvisor.gas_backend.model

import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Entity
@Table(name = "user")
data class User(

    @Column(unique = true, nullable = false)
    var username: String,

    @NotBlank
    @Size(min = 6)
    @Column(nullable = false)
    var password: String,

    @NotBlank
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    var role: Role

) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0
}
