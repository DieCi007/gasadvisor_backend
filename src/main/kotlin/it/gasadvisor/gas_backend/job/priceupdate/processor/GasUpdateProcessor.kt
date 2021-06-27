package it.gasadvisor.gas_backend.job.priceupdate.processor

sealed interface GasUpdateProcessor {
    fun update()
}
