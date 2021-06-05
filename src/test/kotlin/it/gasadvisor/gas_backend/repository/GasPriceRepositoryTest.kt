package it.gasadvisor.gas_backend.repository

import it.gasadvisor.gas_backend.model.GasPrice
import it.gasadvisor.gas_backend.model.GasPriceId
import it.gasadvisor.gas_backend.model.GasStation
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.TestPropertySource
import java.time.Instant

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("classpath:application-test.properties")
class GasPriceRepositoryTest @Autowired constructor(
    val priceRepository: GasPriceRepository,
    val stationRepository: GasStationRepository
) {
    private val station = GasStation(
        1, "diego", "", "", "", "",
        "", "", 1.1, 1.1,
    )
    private val timeRead: Instant = Instant.now()
    private val priceOne = GasPrice(
        GasPriceId(
            GasStation(1), true, timeRead,
            "desc"
        ), 1.87
    )

    private val priceTwo = GasPrice(
        GasPriceId(
            GasStation(1), true, timeRead,
            "desc"
        ), 2.87
    )

    @Test
    fun `save should update station`() {
        stationRepository.save(station)
        priceRepository.save(priceOne)
        val second = priceRepository.save(priceTwo)
        val fromDb = priceRepository.findById(
            GasPriceId(
                GasStation(1),
                true, timeRead, "desc"
            )
        ).get()
        Assertions.assertEquals(second.price, fromDb.price)
    }
}
