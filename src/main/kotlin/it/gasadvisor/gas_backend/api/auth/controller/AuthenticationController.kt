package it.gasadvisor.gas_backend.api.auth.controller

import it.gasadvisor.gas_backend.api.auth.contract.AuthenticationRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class AuthenticationController {

    @PostMapping("/api/v1/login")
    @ResponseStatus(HttpStatus.OK)
    fun login(@RequestBody @Valid request: AuthenticationRequest) {

    }
}
