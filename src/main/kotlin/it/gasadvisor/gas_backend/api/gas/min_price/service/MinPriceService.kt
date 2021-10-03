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

    fun update(req: UpdateMinPriceRequest) {
        val price = repo.findByType(req.type).orElseGet { FuelMinPrice(null, req.type, req.minPrice) }
        price.minPrice = req.minPrice
        repo.save(price)
    }
}
