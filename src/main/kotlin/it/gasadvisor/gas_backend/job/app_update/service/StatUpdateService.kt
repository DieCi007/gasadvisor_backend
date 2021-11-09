package it.gasadvisor.gas_backend.job.app_update.service

import it.gasadvisor.gas_backend.util.logging.Log

abstract class StatUpdateService<T> {
    companion object : Log()

    open fun update() {
        val timeInit = System.currentTimeMillis()
        val features = buildFeatures()
        val updateList = mutableListOf<T>()
        features.forEachIndexed { index, feature ->
            if (updateList.size == 500) {
                updateList.clear()
            }
            updateList.add(feature)
            if (updateList.size == 500 || index == features.lastIndex) {
                try {
                    save(updateList)
                } catch (e: Exception) {
                    log.error("Batch save error, {}", e.message)
                }
            }
        }
        log.info("Operation completed in ${(System.currentTimeMillis() - timeInit) / 1000} seconds")
        return
    }

    abstract fun save(features: List<T>)
    abstract fun buildFeatures(): List<T>
}
