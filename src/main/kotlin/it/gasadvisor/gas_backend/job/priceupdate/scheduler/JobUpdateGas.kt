package it.gasadvisor.gas_backend.job.priceupdate.scheduler

import it.gasadvisor.gas_backend.job.priceupdate.processor.ExplicitFuelUpdateProcessor
import it.gasadvisor.gas_backend.job.priceupdate.processor.GasStationUpdateProcessor
import it.gasadvisor.gas_backend.job.priceupdate.processor.PriceUpdateProcessor
import it.gasadvisor.gas_backend.job.priceupdate.processor.ProvinceUpdateProcessor
import it.gasadvisor.gas_backend.util.Log
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class JobUpdateGas @Autowired constructor(
    @Qualifier("gasStationUpdateProcessor") private val stationPriceUpdateProcessor: GasStationUpdateProcessor,
    @Qualifier("priceUpdateProcessor") private val priceUpdateProcessor: PriceUpdateProcessor,
    @Qualifier("provinceUpdateProcessor") private val provinceUpdateProcessor: ProvinceUpdateProcessor,
    @Qualifier("explicitFuelUpdateProcessor") private val explicitFuelUpdateProcessor: ExplicitFuelUpdateProcessor
) {
    companion object : Log()

    @Scheduled(cron = "\${job.update.gas.cron}", zone = "Europe/Rome")
    fun init() {
        log.info("Starting station update")
        stationPriceUpdateProcessor.update()
        log.info("Starting price update")
        priceUpdateProcessor.update()
        log.info("Starting province update")
        provinceUpdateProcessor.update()
        log.info("Starting fuel type update")
        explicitFuelUpdateProcessor.update()
    }

}
