package it.gasadvisor.gas_backend.api.gas.explicit_fuel.controller

import it.gasadvisor.gas_backend.api.gas.explicit_fuel.contract.AssignFuelRequest
import it.gasadvisor.gas_backend.api.gas.explicit_fuel.contract.IExplicitFuelType
import it.gasadvisor.gas_backend.api.gas.explicit_fuel.service.ExplicitFuelService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = ["/api/v1/gas/explicit-fuel"])
class ExplicitFuelController @Autowired constructor(
    private val service: ExplicitFuelService
) {
    @GetMapping
    @PreAuthorize("hasAuthority(\"READ_ALL\")")
    @ResponseStatus(HttpStatus.OK)
    fun findAll(): List<IExplicitFuelType> {
        return service.adminGetAll()
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority(\"WRITE_ALL\")")
    @ResponseStatus(HttpStatus.OK)
    fun assign(
        @PathVariable("id") id: Long,
        @RequestBody request: AssignFuelRequest
    ): IExplicitFuelType {
        return service.assign(id, request)
    }


}
