package it.gasadvisor.gas_backend.api.gas.min_price.controller

import it.gasadvisor.gas_backend.api.gas.min_price.contract.UpdateMinPriceRequest
import it.gasadvisor.gas_backend.api.gas.min_price.service.MinPriceService
import it.gasadvisor.gas_backend.model.FuelMinPrice
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = ["/api/v1/gas/minprice"])
class MinPriceController @Autowired constructor(
    private val service: MinPriceService
) {
    @PatchMapping
    @PreAuthorize("hasAuthority(\"WRITE_ALL\")")
    @ResponseStatus(HttpStatus.OK)
    fun updateMinPrice(@RequestBody request: UpdateMinPriceRequest) {
        return service.update(request)
    }

    @GetMapping
    @PreAuthorize("hasAuthority(\"READ_ALL\")")
    @ResponseStatus(HttpStatus.OK)
    fun getMinPrices(): List<FuelMinPrice> {
        return service.getAll()
    }
}
