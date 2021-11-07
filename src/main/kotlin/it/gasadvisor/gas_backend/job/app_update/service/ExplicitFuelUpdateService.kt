package it.gasadvisor.gas_backend.job.app_update.service

import it.gasadvisor.gas_backend.model.entities.ExplicitFuelType
import it.gasadvisor.gas_backend.repository.ExplicitFuelRepository
import it.gasadvisor.gas_backend.repository.GasPriceRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ExplicitFuelUpdateService @Autowired constructor(
    private val fuelRepository: ExplicitFuelRepository,
    private val priceRepository: GasPriceRepository
) : StatUpdateService<ExplicitFuelType>() {

    override fun save(features: List<ExplicitFuelType>) {
        fuelRepository.saveAll(features)
    }

    override fun buildFeatures(): List<ExplicitFuelType> {
        return priceRepository.findNotSavedFuelTypes().map { ExplicitFuelType(it) }
    }
}
