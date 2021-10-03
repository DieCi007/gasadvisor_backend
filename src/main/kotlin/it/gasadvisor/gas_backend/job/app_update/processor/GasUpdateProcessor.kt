package it.gasadvisor.gas_backend.job.app_update.processor

sealed interface GasUpdateProcessor {
    fun update()
}
