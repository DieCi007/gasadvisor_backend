package it.gasadvisor.gas_backend.api.test

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
class Controller {

    @GetMapping("test")
    @ResponseStatus(HttpStatus.OK)
    fun test(): String {

        return "ok"
    }
}
