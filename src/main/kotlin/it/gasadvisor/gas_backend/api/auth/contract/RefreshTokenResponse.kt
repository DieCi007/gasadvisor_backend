package it.gasadvisor.gas_backend.api.auth.contract

data class RefreshTokenResponse constructor(
    val authToken: String,
    val refreshToken: String,
)
