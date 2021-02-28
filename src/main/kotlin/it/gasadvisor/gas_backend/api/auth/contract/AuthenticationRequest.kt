package it.gasadvisor.gas_backend.api.auth.contract

import com.fasterxml.jackson.annotation.JsonProperty

data class AuthenticationRequest constructor(
    @JsonProperty("username")
    val username: String,

    @JsonProperty("password")
    val password: String
)
