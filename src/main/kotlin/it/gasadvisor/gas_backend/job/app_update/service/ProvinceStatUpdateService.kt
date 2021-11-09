package it.gasadvisor.gas_backend.job.app_update.service

import it.gasadvisor.gas_backend.model.entities.PriceStat
import it.gasadvisor.gas_backend.model.enums.PriceStatType
import it.gasadvisor.gas_backend.model.entities.ProvinceStat
import it.gasadvisor.gas_backend.model.enums.CommonFuelType
import it.gasadvisor.gas_backend.repository.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class ProvinceStatUpdateService @Autowired constructor(
    private val provinceStatRepository: ProvinceStatRepository,
    private val priceRepository: GasPriceRepository,
    private val provinceRepository: ProvinceRepository,
    private val priceStatRepository: PriceStatRepository,
    private val municipalityRepository: MunicipalityRepository
) : StatUpdateService<ProvinceStat>() {
    override fun save(features: List<ProvinceStat>) {
        provinceStatRepository.saveAll(features)
    }

    override fun buildFeatures(): List<ProvinceStat> {
        val date = Instant.now().truncatedTo(ChronoUnit.DAYS)
        val provinces = provinceRepository.findAll()
        provinces.forEach { p ->
            val iMunicipalityLeastStations = municipalityRepository.findOneWithLeastStations(p.name)
            val municipalityLeastStations = municipalityRepository.findByNameAndProvince(
                iMunicipalityLeastStations.getMunicipality(),
                iMunicipalityLeastStations.getProvince()
            ).orElse(null)

            val iMunicipalityMostStations = municipalityRepository.findOneWithMostStations(p.name)
            val municipalityMostStations = municipalityRepository.findByNameAndProvince(
                iMunicipalityMostStations.getMunicipality(),
                iMunicipalityMostStations.getProvince()
            ).orElse(null)

            var provinceStat = ProvinceStat(date, municipalityMostStations, municipalityLeastStations, p)

            try {
                provinceStat = provinceStatRepository.save(provinceStat)
            } catch (e: Exception) {
                log.error("Could not save province stat with date: {}, error: {}", provinceStat.date, e.message)
                return@forEach
            }
            val priceList = CommonFuelType.values()
                .map {
                    val iPriceStat = priceRepository.findPriceStat(it, p.name, null)
                    listOf(
                        PriceStat(
                            null,
                            it,
                            iPriceStat.getAvg(),
                            PriceStatType.AVG,
                            null,
                            provinceStat
                        ),
                        PriceStat(
                            null,
                            it,
                            iPriceStat.getMin(),
                            PriceStatType.MIN,
                            null,
                            provinceStat
                        ),
                        PriceStat(
                            null,
                            it,
                            iPriceStat.getMax(),
                            PriceStatType.MAX,
                            null,
                            provinceStat
                        )
                    )
                }.flatten()
            try {
                priceStatRepository.saveAll(priceList)
            } catch (e: Exception) {
                log.error("Could not save price list for province stat with date: {}. Error: {}", provinceStat.date, e.message)
            }
        }
        return emptyList()
    }
}
