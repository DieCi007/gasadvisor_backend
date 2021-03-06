package it.gasadvisor.gas_backend.job.app_update.processor

import it.gasadvisor.gas_backend.job.app_update.service.ExplicitFuelUpdateService
import it.gasadvisor.gas_backend.util.logging.Log
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
@Qualifier("explicitFuelUpdateProcessor")
class ExplicitFuelUpdateProcessor @Autowired constructor(
    private val service: ExplicitFuelUpdateService
) : GasUpdateProcessor {
    companion object : Log()

    override fun update() {
        try {
            service.update()
        } catch (e: Exception) {
            log.info("Exception during fuel type update")
            log.error(e.message, e)
        }
    }

}
