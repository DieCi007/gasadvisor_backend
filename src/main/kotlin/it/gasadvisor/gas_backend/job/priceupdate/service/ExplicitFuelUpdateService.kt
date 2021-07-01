package it.gasadvisor.gas_backend.job.priceupdate.service

import it.gasadvisor.gas_backend.repository.ExplicitFuelRepository
import it.gasadvisor.gas_backend.util.Log
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ExplicitFuelUpdateService @Autowired constructor(
    private val fuelRepository: ExplicitFuelRepository
) {
    companion object : Log()

    fun update() {
        val timeInit = System.currentTimeMillis()





        log.info("Operation completed in ${(System.currentTimeMillis() - timeInit) / 1000} seconds")
    }
}
