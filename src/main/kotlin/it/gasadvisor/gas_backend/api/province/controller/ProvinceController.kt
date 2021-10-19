package it.gasadvisor.gas_backend.api.province.controller

import it.gasadvisor.gas_backend.api.province.service.ProvinceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/api/v1/province"])
class ProvinceController @Autowired constructor(
    private val service: ProvinceService
) {
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAll(): List<String> {
        return service.getAll()
    }
}
