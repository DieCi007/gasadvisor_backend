package it.gasadvisor.gas_backend.job.app_update.processor

import it.gasadvisor.gas_backend.job.app_update.service.GasStationUpdateService
import it.gasadvisor.gas_backend.util.logging.Log
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

@Component
@Qualifier("gasStationUpdateProcessor")
class GasStationUpdateProcessor @Autowired constructor(
    private val gasStationService: GasStationUpdateService,
    @Value("\${url.gasstation}") private var endpoint: String
) : GasUpdateProcessor {

    companion object : Log()

    override fun update() {
        try {
            val url = URL(endpoint)
            val inputStreamReader = InputStreamReader(url.openStream())
            val bufferedReader = BufferedReader(inputStreamReader)
            gasStationService.handle(bufferedReader)
        } catch (e: Exception) {
            log.info("Exception during gas station update, will retry")
            log.error(e.message, e)
            retry()
        }
    }

    private fun retry() {
        try {
            val url = URL(endpoint)
            val inputStreamReader = InputStreamReader(url.openStream())
            val bufferedReader = BufferedReader(inputStreamReader)
            gasStationService.handle(bufferedReader)
        } catch (e: Exception) {
            log.info("Second try error while updating gas stations")
            log.error(e.message, e)
        }
    }
}
