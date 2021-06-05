package it.gasadvisor.gas_backend.job.priceupdate.service

import it.gasadvisor.gas_backend.api.gas.price.service.GasPriceService
import it.gasadvisor.gas_backend.exception.NotFoundException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyZeroInteractions
import java.io.BufferedReader
import java.io.InputStreamReader

@ExtendWith(MockitoExtension::class)
internal class PriceUpdateServiceTest {
    @Mock
    lateinit var priceService: GasPriceService
    private val delimiter = ";"
    private lateinit var updateService: PriceUpdateService

    @BeforeEach
    fun setUp() {
        updateService = PriceUpdateService(priceService, delimiter)
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

}
