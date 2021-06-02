package it.gasadvisor.gas_backend.job.priceupdate.scheduler

import it.gasadvisor.gas_backend.job.priceupdate.processor.GasStationUpdateProcessor
import it.gasadvisor.gas_backend.job.priceupdate.processor.PriceUpdateProcessor
import it.gasadvisor.gas_backend.util.Log
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
class JobUpdateGasStationPrice @Autowired constructor(
    @Qualifier("gasStationUpdateProcessor") private val stationPriceUpdateProcessor: GasStationUpdateProcessor,
    @Qualifier("priceUpdateProcessor") private val priceUpdateProcessor: PriceUpdateProcessor
) {
    companion object : Log()

    fun init() {
        log.info("Starting price update job")
        stationPriceUpdateProcessor.update()
        priceUpdateProcessor.update()
    }

}
