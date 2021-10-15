package it.gasadvisor.gas_backend.api.gas.min_price.service

import it.gasadvisor.gas_backend.api.gas.min_price.contract.UpdateMinPriceRequest
import it.gasadvisor.gas_backend.model.entities.FuelMinPrice
import it.gasadvisor.gas_backend.repository.FuelMinPriceRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MinPriceService @Autowired constructor(
    private val repo: FuelMinPriceRepository
) {
    fun getAll(): List<FuelMinPrice> {
        return repo.findAll()
    }

    fun update(req: List<UpdateMinPriceRequest>) {
        req.forEach { price ->
            val saved = repo.findByType(price.type).orElseGet { FuelMinPrice(null, price.type, price.minPrice) }
            saved.minPrice = price.minPrice
            repo.save(saved)
        }

    }
}
