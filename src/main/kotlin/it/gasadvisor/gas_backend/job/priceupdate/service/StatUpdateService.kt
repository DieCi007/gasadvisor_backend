package it.gasadvisor.gas_backend.job.priceupdate.service

import it.gasadvisor.gas_backend.util.Log

abstract class StatUpdateService<T> {
    companion object : Log()

    open fun update() {
        val timeInit = System.currentTimeMillis()
        val features = buildFeatures()
        features.stream().forEach { save(it) }
        log.info("Operation completed in ${(System.currentTimeMillis() - timeInit) / 1000} seconds")
        return
    }

    abstract fun save(feature: T): T
    abstract fun buildFeatures(): List<T>
}
