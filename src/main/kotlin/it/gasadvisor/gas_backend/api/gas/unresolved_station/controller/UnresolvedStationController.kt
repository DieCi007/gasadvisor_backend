package it.gasadvisor.gas_backend.api.gas.unresolved_station.controller

import it.gasadvisor.gas_backend.api.gas.unresolved_station.contract.GetAllUnresolvedResponse
import it.gasadvisor.gas_backend.api.gas.unresolved_station.service.UnresolvedGasStationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = ["/api/v1/gas/unresolved-station"])
class UnresolvedStationController @Autowired constructor(
    private val service: UnresolvedGasStationService
) {
    @GetMapping
    @PreAuthorize("hasAuthority(\"READ_ALL\")")
    @ResponseStatus(HttpStatus.OK)
    fun getUnresolved(): List<GetAllUnresolvedResponse> {
        return service.getAll()
    }

    @PatchMapping("/{id}/resolve")
    @PreAuthorize("hasAuthority(\"WRITE_ALL\")")
    @ResponseStatus(HttpStatus.OK)
    fun resolve(@PathVariable("id") id: Long) {
        return service.resolve(id)
    }
}
