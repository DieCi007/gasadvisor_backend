package it.gasadvisor.gas_backend.api.municipality.controller

import it.gasadvisor.gas_backend.api.municipality.service.MunicipalityService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = ["/api/v1/municipality"])
class MunicipalityController @Autowired constructor(
    private val service: MunicipalityService
) {
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAllFromProvince(
        @RequestParam("province") province: String
    ): List<String> {
        return service.getNamesByProvince(province)
    }
}
