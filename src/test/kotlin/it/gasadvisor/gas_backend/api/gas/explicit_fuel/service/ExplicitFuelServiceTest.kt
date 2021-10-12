package it.gasadvisor.gas_backend.api.gas.explicit_fuel.service

import it.gasadvisor.gas_backend.api.gas.explicit_fuel.contract.AssignAllFuelRequest
import it.gasadvisor.gas_backend.api.gas.explicit_fuel.contract.AssignFuelRequest
import it.gasadvisor.gas_backend.model.entities.ExplicitFuelType
import it.gasadvisor.gas_backend.model.enums.CommonFuelType
import it.gasadvisor.gas_backend.repository.ExplicitFuelRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.AdditionalAnswers.returnsFirstArg
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class ExplicitFuelServiceTest {
    @Mock
    lateinit var repo: ExplicitFuelRepository

    @InjectMocks
    lateinit var service: ExplicitFuelService

    @Captor
    lateinit var captor: ArgumentCaptor<List<ExplicitFuelType>>

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

    @Test
    fun `should assign all types`() {
        val request = listOf(
            AssignAllFuelRequest(CommonFuelType.GASOLIO, listOf(1, 2)),
            AssignAllFuelRequest(CommonFuelType.BENZINA, listOf(3, 4)),
        )

        whenever(repo.findById(1)).thenReturn(
            Optional.of(
                ExplicitFuelType(1, "gas", null)
            )
        )
        whenever(repo.findById(2)).thenReturn(
            Optional.of(
                ExplicitFuelType(2, "olio", CommonFuelType.BENZINA)
            )
        )
        whenever(repo.findById(3)).thenReturn(
            Optional.of(
                ExplicitFuelType(3, "ben", CommonFuelType.GASOLIO)
            )
        )
        whenever(repo.findById(4)).thenReturn(
            Optional.of(
                ExplicitFuelType(4, "zina", null)
            )
        )
        service.assignAllFuels(request)
        verify(repo, times(2)).saveAll(capture(captor))
        val firstBatch = captor.firstValue
        val secondBatch = captor.secondValue
        assertEquals(CommonFuelType.GASOLIO, firstBatch[0].commonType)
        assertEquals("gas", firstBatch[0].name)
        assertEquals(CommonFuelType.GASOLIO, firstBatch[1].commonType)
        assertEquals("olio", firstBatch[1].name)
        assertEquals(2, firstBatch.size)
        assertEquals(CommonFuelType.BENZINA, secondBatch[0].commonType)
        assertEquals("ben", secondBatch[0].name)
        assertEquals(CommonFuelType.BENZINA, secondBatch[1].commonType)
        assertEquals("zina", secondBatch[1].name)
        assertEquals(2, secondBatch.size)
    }
}
