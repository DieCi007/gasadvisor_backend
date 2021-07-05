package it.gasadvisor.gas_backend.job.priceupdate.service

import it.gasadvisor.gas_backend.model.*
import it.gasadvisor.gas_backend.repository.GasPriceRepository
import it.gasadvisor.gas_backend.repository.MunicipalityRepository
import it.gasadvisor.gas_backend.repository.MunicipalityStatRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class MunicipalityStatUpdateService @Autowired constructor(
    private val municipalityStatRepository: MunicipalityStatRepository,
    private val priceRepository: GasPriceRepository,
    private val municipalityRepository: MunicipalityRepository

) : StatUpdateService<MunicipalityStat>() {
    override fun save(feature: MunicipalityStat) {
        municipalityStatRepository.save(feature)
    }

    override fun buildFeatures(): List<MunicipalityStat> {
        val date = Instant.now().truncatedTo(ChronoUnit.DAYS)
        val municipalities = municipalityRepository.findAll()
        return municipalities.map { m ->
            val stats = CommonFuelType.values()
                .flatMap {
                    val iPriceStat = priceRepository.findPriceStat(it, m.province.name, m.name)
                    listOf(
                        PriceStat(it, iPriceStat.getAvg(), PriceStatType.AVG),
                        PriceStat(it, iPriceStat.getMin(), PriceStatType.MIN),
                        PriceStat(it, iPriceStat.getMax(), PriceStatType.MAX),
                    )
                }
            MunicipalityStat(null, date, stats, m)
        }
    }
}
