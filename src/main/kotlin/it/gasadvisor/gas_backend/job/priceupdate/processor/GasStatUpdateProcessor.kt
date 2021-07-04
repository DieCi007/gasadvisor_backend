package it.gasadvisor.gas_backend.job.priceupdate.processor

import it.gasadvisor.gas_backend.job.priceupdate.service.GasStatUpdateService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
@Qualifier("gasStatUpdateProcessor")
class GasStatUpdateProcessor @Autowired constructor(
    private val gasStatUpdateService: GasStatUpdateService
) : GasUpdateProcessor {
    override fun update() {
        try {
            gasStatUpdateService.update()
        } catch (e: Exception) {
            PriceUpdateProcessor.log.info("Exception during gas stat update")
            PriceUpdateProcessor.log.error(e.message, e)
        }
    }

}
