package it.gasadvisor.gas_backend.job.priceupdate.processor

import it.gasadvisor.gas_backend.job.priceupdate.service.PriceUpdateService
import it.gasadvisor.gas_backend.util.Log
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

@Component
@Qualifier("priceUpdateProcessor")
class PriceUpdateProcessor @Autowired constructor(
    private val priceUpdateService: PriceUpdateService
) : GasStationPriceUpdateProcessor {
    @Value("\${url.price}")
    lateinit var endpoint: String

    companion object : Log()

    override fun update() {
        try {
            val url = URL(endpoint)
            val inputStreamReader = InputStreamReader(url.openStream())
            val bufferedReader = BufferedReader(inputStreamReader)
            priceUpdateService.handle(bufferedReader)
        } catch (e: Exception) {
            log.info("Exception during gas price update")
            log.error(e.message, e)
        }
    }
}
