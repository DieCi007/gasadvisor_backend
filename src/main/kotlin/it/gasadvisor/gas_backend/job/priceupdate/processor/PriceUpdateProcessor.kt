package it.gasadvisor.gas_backend.job.priceupdate.processor

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
@Qualifier("priceUpdateProcessor")
class PriceUpdateProcessor : GasStationPriceUpdateProcessor {
    override fun update() {
        println("diego")
    }
}
