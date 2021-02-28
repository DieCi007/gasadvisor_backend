package it.gasadvisor.gas_backend.api.test

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller {

    @GetMapping("public/test")
    @ResponseStatus(HttpStatus.OK)
    fun test(): String {

        return "ok"
    }
}
