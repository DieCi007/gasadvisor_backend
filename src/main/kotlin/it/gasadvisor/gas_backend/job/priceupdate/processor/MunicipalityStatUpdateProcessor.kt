package it.gasadvisor.gas_backend.job.priceupdate.processor

import it.gasadvisor.gas_backend.job.priceupdate.service.MunicipalityStatUpdateService
import it.gasadvisor.gas_backend.util.Log
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import java.lang.Exception

@Component
@Qualifier("municipalityStatUpdateProcessor")
class MunicipalityStatUpdateProcessor @Autowired constructor(
    private val municipalityStatUpdateService: MunicipalityStatUpdateService
) : GasUpdateProcessor {
    companion object : Log()

    override fun update() {
        try {
            municipalityStatUpdateService.update()
        } catch (e: Exception) {
            log.info("Exception during municipality stat update")
            log.error(e.message, e)
        }
    }
}
