package it.gasadvisor.gas_backend.job.app_update.service

import it.gasadvisor.gas_backend.api.gas.explicit_fuel.service.ExplicitFuelService
import it.gasadvisor.gas_backend.api.gas.min_price.service.MinPriceService
import it.gasadvisor.gas_backend.api.gas.price.service.GasPriceService
import it.gasadvisor.gas_backend.exception.NotFoundException
import it.gasadvisor.gas_backend.model.CommonFuelType
import it.gasadvisor.gas_backend.model.ExplicitFuelType
import it.gasadvisor.gas_backend.model.FuelMinPrice
import it.gasadvisor.gas_backend.model.GasPrice
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*
import java.io.BufferedReader
import java.io.InputStreamReader


@ExtendWith(MockitoExtension::class)
internal class PriceUpdateServiceTest {
    @Mock
    lateinit var priceService: GasPriceService

    @Mock
    lateinit var explicitFuelService: ExplicitFuelService

    @Mock
    lateinit var minPriceService: MinPriceService

    @Captor
    lateinit var captor: ArgumentCaptor<List<GasPrice>>

    private val delimiter = ";"
    private lateinit var updateService: PriceUpdateService

    @BeforeEach
    fun setUp() {
        updateService = PriceUpdateService(
            priceService, explicitFuelService,
            minPriceService, delimiter
        )
    }

    @Test
    fun `should save all when correct csv`() {
        val fileStream = this::class.java.classLoader.getResourceAsStream("fixtures/price/price-correct.csv")
            ?: throw NotFoundException("file not found")
        val bufferedReader = BufferedReader(InputStreamReader(fileStream))
        updateService.handle(bufferedReader)

        verify(priceService, times(1)).saveAll(any())
    }

    @Test
    fun `should not save when bad formatted data`() {
        val fileStream = this::class.java.classLoader.getResourceAsStream("fixtures/price/price-incorrect.csv")
            ?: throw NotFoundException("file not found")
        val bufferedReader = BufferedReader(InputStreamReader(fileStream))
        updateService.handle(bufferedReader)

        verifyZeroInteractions(priceService)
    }

    @Test
    fun `should skip on bad price`() {
        whenever(minPriceService.getAll()).thenReturn(
            listOf(
                FuelMinPrice(null, CommonFuelType.GASOLIO, 1.1)
            )
        )
        whenever(explicitFuelService.getAll()).thenReturn(
            listOf(
                ExplicitFuelType(null, "Hi-Q Diesel", CommonFuelType.GASOLIO)
            )
        )
        val fileStream = this::class.java.classLoader.getResourceAsStream("fixtures/price/price-correct.csv")
            ?: throw NotFoundException("file not found")
        val bufferedReader = BufferedReader(InputStreamReader(fileStream))
        updateService.handle(bufferedReader)
        verify(priceService, times(1)).saveAll(capture(captor))
        assertEquals(2, captor.value.size)
    }

    @Test
    fun `should not skip on good price`() {
        whenever(minPriceService.getAll()).thenReturn(
            listOf(
                FuelMinPrice(null, CommonFuelType.GASOLIO, 0.676)
            )
        )
        whenever(explicitFuelService.getAll()).thenReturn(
            listOf(
                ExplicitFuelType(null, "Hi-Q Diesel", CommonFuelType.GASOLIO)
            )
        )
        val fileStream = this::class.java.classLoader.getResourceAsStream("fixtures/price/price-correct.csv")
            ?: throw NotFoundException("file not found")
        val bufferedReader = BufferedReader(InputStreamReader(fileStream))
        updateService.handle(bufferedReader)
        verify(priceService, times(1)).saveAll(capture(captor))
        assertEquals(4, captor.value.size)
    }

}
