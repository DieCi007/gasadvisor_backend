package it.gasadvisor.gas_backend.job.priceupdate.processor

sealed interface GasStationPriceUpdateProcessor {
    fun update()
}
