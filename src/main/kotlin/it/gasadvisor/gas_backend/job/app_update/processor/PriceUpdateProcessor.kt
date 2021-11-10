package it.gasadvisor.gas_backend.job.app_update.processor

import it.gasadvisor.gas_backend.job.app_update.service.PriceUpdateService
import it.gasadvisor.gas_backend.util.logging.Log
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.File
import java.net.URL
import java.nio.file.Files

@Component
@Qualifier("priceUpdateProcessor")
class PriceUpdateProcessor @Autowired constructor(
    private val priceUpdateService: PriceUpdateService,
    @Value("\${url.price}") private var endpoint: String
) : GasUpdateProcessor {

    companion object : Log()

    override fun update() {
        val filename = "prices.csv"
        val file = File(filename)
        try {
            val url = URL(endpoint)
            val inputStream = url.openStream()
            Files.copy(inputStream, file.toPath())
            priceUpdateService.handle(file.bufferedReader())
            if (file.exists()) {
                file.delete()
            }
        } catch (e: Exception) {
            if (file.exists()) {
                file.delete()
            }
            log.info("Exception during gas price update, will retry")
            log.error(e.message, e)
            retry()
        }
    }

    private fun retry() {
        val filename = "prices.csv"
        val file = File(filename)
        try {
            val url = URL(endpoint)
            val inputStream = url.openStream()
            Files.copy(inputStream, file.toPath())
            priceUpdateService.handle(file.bufferedReader())
        } catch (e: Exception) {
            if (file.exists()) {
                file.delete()
            }
            log.info("Second try error while updating gas stations")
            log.error(e.message, e)
        }
    }

}
