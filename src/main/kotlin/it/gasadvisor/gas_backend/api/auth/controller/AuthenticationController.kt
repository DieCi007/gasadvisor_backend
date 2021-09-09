package it.gasadvisor.gas_backend.api.auth.controller

import it.gasadvisor.gas_backend.api.auth.contract.AuthenticationRequest
import it.gasadvisor.gas_backend.api.auth.contract.RefreshTokenRequest
import it.gasadvisor.gas_backend.api.auth.contract.RefreshTokenResponse
import it.gasadvisor.gas_backend.api.auth.service.AuthenticationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
class AuthenticationController @Autowired constructor
    (val service: AuthenticationService) {

    @PostMapping("/api/v1/login")
    @ResponseStatus(HttpStatus.OK)
    fun login(@RequestBody @Valid request: AuthenticationRequest) {
    }

    @PostMapping("/api/v1/refresh")
    @ResponseStatus(HttpStatus.OK)
    fun refresh(@RequestBody @Valid request: RefreshTokenRequest): RefreshTokenResponse {
        return service.refreshToken(request)
    }


}
