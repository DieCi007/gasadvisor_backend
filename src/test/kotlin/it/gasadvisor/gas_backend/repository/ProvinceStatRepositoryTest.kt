package it.gasadvisor.gas_backend.repository

import it.gasadvisor.gas_backend.model.entities.Province
import it.gasadvisor.gas_backend.model.entities.ProvinceStat
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.Instant
import java.time.temporal.ChronoUnit

@JpaTest
class ProvinceStatRepositoryTest @Autowired constructor(
    private val repo: ProvinceStatRepository,
    private val provinceRepo: ProvinceRepository
) {

    @Test
    fun `should find by date and province name`() {
        val today = Instant.now().truncatedTo(ChronoUnit.DAYS)
        val yesterday = Instant.now().minus(1, ChronoUnit.DAYS).truncatedTo(ChronoUnit.DAYS)
        val mi = provinceRepo.save(Province("mi"))
        val to = provinceRepo.save(Province("to"))
        repo.saveAll(
            listOf(
                ProvinceStat(today, mi),
                ProvinceStat(today, to),
                ProvinceStat(yesterday, mi)
            )
        )
        val first = repo.findByDateAndProvinceName(today, "mi")
        assertTrue(first[0].province.name == "mi")
        val second = repo.findByDateAndProvinceName(today, "to")
        assertTrue(second[0].province.name == "to")
        val third = repo.findByDateAndProvinceName(yesterday, "mi")
        assertTrue(third[0].province.name == "mi")
    }

    @BeforeEach
    fun reset() {
        repo.deleteAllInBatch()
    }
}
