package it.gasadvisor.gas_backend.repository

import it.gasadvisor.gas_backend.model.entities.FuelMinPrice
import it.gasadvisor.gas_backend.model.enums.CommonFuelType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@JpaTest
class FuelMinPriceRepositoryTest @Autowired constructor(
    val repo: FuelMinPriceRepository
) {
    @Test
    fun `should find by type`() {
        val price = FuelMinPrice(null, CommonFuelType.GASOLIO, 1.8)
        repo.save(price)
        val response = repo.findByType(CommonFuelType.GASOLIO)
        assertEquals(1.8, response.get().minPrice)
    }
}
