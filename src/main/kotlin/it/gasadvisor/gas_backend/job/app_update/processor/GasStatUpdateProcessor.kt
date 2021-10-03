package it.gasadvisor.gas_backend.job.app_update.processor

import it.gasadvisor.gas_backend.job.app_update.service.GasStatUpdateService
import it.gasadvisor.gas_backend.util.Log
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
@Qualifier("gasStatUpdateProcessor")
class GasStatUpdateProcessor @Autowired constructor(
    private val gasStatUpdateService: GasStatUpdateService
) : GasUpdateProcessor {
    companion object : Log()

    override fun update() {
        try {
            gasStatUpdateService.update()
        } catch (e: Exception) {
            log.info("Exception during gas stat update")
            log.error(e.message, e)
        }
    }

}
