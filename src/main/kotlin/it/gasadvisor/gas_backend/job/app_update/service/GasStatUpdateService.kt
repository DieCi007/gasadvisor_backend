package it.gasadvisor.gas_backend.job.app_update.service

import it.gasadvisor.gas_backend.model.entities.GasStat
import it.gasadvisor.gas_backend.model.entities.PriceStat
import it.gasadvisor.gas_backend.model.enums.CommonFuelType
import it.gasadvisor.gas_backend.model.enums.PriceStatType
import it.gasadvisor.gas_backend.repository.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class GasStatUpdateService @Autowired constructor(
    private val gasStatRepository: GasStatRepository,
    private val gasPriceRepository: GasPriceRepository,
    private val provinceRepository: ProvinceRepository,
    private val municipalityRepository: MunicipalityRepository,
    private val priceStatRepository: PriceStatRepository
) : StatUpdateService<GasStat>() {

    override fun save(features: List<GasStat>) {
        gasStatRepository.saveAll(features)
    }

    override fun buildFeatures(): List<GasStat> {
        val date = Instant.now().truncatedTo(ChronoUnit.DAYS)
        val provinceMostStationsName = provinceRepository.findOneWithMostStations().getProvince()
        val provinceMostStations = provinceRepository
            .findByName(provinceMostStationsName).orElse(null)

        val provinceLeastStationsName = provinceRepository.findOneWithLeastStations().getProvince()
        val provinceLeastStation = provinceRepository
            .findByName(provinceLeastStationsName).orElse(null)

        val iMunicipalityMostStations = municipalityRepository.findOneWithMostStations()
        val municipalityMostStations = municipalityRepository
            .findByNameAndProvince(
                iMunicipalityMostStations.getMunicipality(),
                iMunicipalityMostStations.getProvince()
            ).orElse(null)

        val iMunicipalityLeastStations = municipalityRepository.findOneWithLeastStations()
        val municipalityLeastStation = municipalityRepository
            .findByNameAndProvince(
                iMunicipalityLeastStations.getMunicipality(),
                iMunicipalityLeastStations.getProvince()
            ).orElse(null)
        var gasStat = GasStat(
            null, date, provinceMostStations,
            municipalityMostStations, provinceLeastStation,
            municipalityLeastStation, emptyList()
        )
        try {
            gasStat = gasStatRepository.save(gasStat)
        } catch (e: Exception) {
            log.error("Could not save gasStat with date: {}, error: {}", gasStat.date, e)
        }

        CommonFuelType.values()
            .forEach {
                val iPriceStat = gasPriceRepository.findPriceStat(it, null, null)
                val priceList = listOf(
                    PriceStat(
                        null,
                        it,
                        iPriceStat.getAvg(),
                        PriceStatType.AVG,
                        gasStat,
                        null
                    ),
                    PriceStat(
                        null,
                        it,
                        iPriceStat.getMax(),
                        PriceStatType.MAX,
                        gasStat,
                        null
                    ),
                    PriceStat(
                        null,
                        it,
                        iPriceStat.getMin(),
                        PriceStatType.MIN,
                        gasStat,
                        null
                    )
                )
                try {
                    priceStatRepository.saveAll(priceList)
                } catch (e: Exception) {
                    log.error("Could not save price list for gasStat with date: {}, error: {}", gasStat.date, e)
                }
            }
        return emptyList()
    }
}
