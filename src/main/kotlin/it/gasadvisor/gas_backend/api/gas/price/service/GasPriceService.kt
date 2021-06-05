package it.gasadvisor.gas_backend.api.gas.price.service

import it.gasadvisor.gas_backend.model.GasPrice
import it.gasadvisor.gas_backend.repository.GasPriceRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class GasPriceService @Autowired constructor(
    private val repository: GasPriceRepository
) {

    @Transactional
    fun saveAll(prices: List<GasPrice>) {
        repository.saveAll(prices)
    }
}
