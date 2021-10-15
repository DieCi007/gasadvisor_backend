package it.gasadvisor.gas_backend.api.gas.min_price.service

import it.gasadvisor.gas_backend.api.gas.min_price.contract.UpdateMinPriceRequest
import it.gasadvisor.gas_backend.model.entities.FuelMinPrice
import it.gasadvisor.gas_backend.model.enums.CommonFuelType
import it.gasadvisor.gas_backend.repository.FuelMinPriceRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class MinPriceServiceTest {
    @Mock
    lateinit var repo: FuelMinPriceRepository

    @InjectMocks
    lateinit var service: MinPriceService

    @Test
    fun `should update min price`() {
        val saved = FuelMinPrice(1, CommonFuelType.GASOLIO, 1.1)
        whenever(repo.findByType(CommonFuelType.GASOLIO)).thenReturn(Optional.of(saved))
        service.update(listOf(UpdateMinPriceRequest(CommonFuelType.GASOLIO, 1.7)))
        verify(repo).save(saved.copy(minPrice = 1.7))
    }
}
