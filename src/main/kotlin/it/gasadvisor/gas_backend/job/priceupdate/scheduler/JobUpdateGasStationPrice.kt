package it.gasadvisor.gas_backend.job.priceupdate.scheduler

import it.gasadvisor.gas_backend.job.priceupdate.processor.GasStationUpdateProcessor
import it.gasadvisor.gas_backend.job.priceupdate.processor.PriceUpdateProcessor
import it.gasadvisor.gas_backend.util.Log
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class JobUpdateGasStationPrice @Autowired constructor(
    @Qualifier("gasStationUpdateProcessor") private val stationPriceUpdateProcessor: GasStationUpdateProcessor,
    @Qualifier("priceUpdateProcessor") private val priceUpdateProcessor: PriceUpdateProcessor
) {
    companion object : Log()

    @Scheduled(cron = "\${job.update.gas.cron}", zone = "Europe/Rome")
    fun init() {
        log.info("Starting price update job")
        stationPriceUpdateProcessor.update()
        priceUpdateProcessor.update()
    }

}