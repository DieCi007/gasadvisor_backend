package it.gasadvisor.gas_backend.api.auth.contract

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class RefreshTokenRequest constructor(
    @NotNull
    @NotBlank
    val token: String
)
