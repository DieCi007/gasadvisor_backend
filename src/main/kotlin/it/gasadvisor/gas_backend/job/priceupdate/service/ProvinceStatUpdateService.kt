package it.gasadvisor.gas_backend.job.priceupdate.service

import it.gasadvisor.gas_backend.model.CommonFuelType
import it.gasadvisor.gas_backend.model.PriceStat
import it.gasadvisor.gas_backend.model.PriceStatType
import it.gasadvisor.gas_backend.model.ProvinceStat
import it.gasadvisor.gas_backend.repository.GasPriceRepository
import it.gasadvisor.gas_backend.repository.PriceStatRepository
import it.gasadvisor.gas_backend.repository.ProvinceRepository
import it.gasadvisor.gas_backend.repository.ProvinceStatRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class ProvinceStatUpdateService @Autowired constructor(
    private val provinceStatRepository: ProvinceStatRepository,
    private val priceRepository: GasPriceRepository,
    private val provinceRepository: ProvinceRepository,
    private val priceStatRepository: PriceStatRepository
) : StatUpdateService<ProvinceStat>() {
    override fun save(feature: ProvinceStat): ProvinceStat {
        return provinceStatRepository.save(feature)
    }

    override fun buildFeatures(): List<ProvinceStat> {
        val date = Instant.now().truncatedTo(ChronoUnit.DAYS)
        val provinces = provinceRepository.findAll()
        provinces.forEach { p ->
            var provinceStat = ProvinceStat(date, p)
            provinceStat = save(provinceStat)
            CommonFuelType.values()
                .forEach {
                    val iPriceStat = priceRepository.findPriceStat(it, p.name, null)
                    priceStatRepository.save(
                        PriceStat(
                            null,
                            it,
                            iPriceStat.getAvg(),
                            PriceStatType.AVG,
                            null,
                            provinceStat
                        )
                    )
                    priceStatRepository.save(
                        PriceStat(
                            null,
                            it,
                            iPriceStat.getMin(),
                            PriceStatType.MIN,
                            null,
                            provinceStat
                        )
                    )
                    priceStatRepository.save(
                        PriceStat(
                            null,
                            it,
                            iPriceStat.getMax(),
                            PriceStatType.MAX,
                            null,
                            provinceStat
                        )
                    )
                }
        }
        return emptyList()
    }
}
