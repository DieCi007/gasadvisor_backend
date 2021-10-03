package it.gasadvisor.gas_backend.api.gas.explicit_fuel.service

import it.gasadvisor.gas_backend.api.gas.explicit_fuel.contract.AssignFuelRequest
import it.gasadvisor.gas_backend.model.entities.ExplicitFuelType
import it.gasadvisor.gas_backend.model.enums.CommonFuelType
import it.gasadvisor.gas_backend.repository.ExplicitFuelRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.AdditionalAnswers.returnsFirstArg
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class ExplicitFuelServiceTest {
    @Mock
    lateinit var repo: ExplicitFuelRepository

    @InjectMocks
    lateinit var service: ExplicitFuelService

    @Test
    fun `should assing common type`() {
        whenever(repo.findById(1)).thenReturn(
            Optional.of(
                ExplicitFuelType(1, "gas", null)
            )
        )
        whenever(repo.save(any())).then(returnsFirstArg<ExplicitFuelType>())
        val result = service.assign(1, AssignFuelRequest(CommonFuelType.GASOLIO))
        assertEquals(1, result.getId())
        assertEquals("gas", result.getName())
        assertEquals(CommonFuelType.GASOLIO, result.getType())
    }
}
