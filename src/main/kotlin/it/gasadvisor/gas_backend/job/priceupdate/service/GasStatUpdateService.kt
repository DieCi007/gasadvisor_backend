package it.gasadvisor.gas_backend.job.priceupdate.service

import it.gasadvisor.gas_backend.model.CommonFuelType
import it.gasadvisor.gas_backend.model.GasStat
import it.gasadvisor.gas_backend.model.PriceStat
import it.gasadvisor.gas_backend.model.PriceStatType
import it.gasadvisor.gas_backend.repository.GasPriceRepository
import it.gasadvisor.gas_backend.repository.GasStatRepository
import it.gasadvisor.gas_backend.repository.MunicipalityRepository
import it.gasadvisor.gas_backend.repository.ProvinceRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class GasStatUpdateService @Autowired constructor(
    private val gasStatRepository: GasStatRepository,
    private val gasPriceRepository: GasPriceRepository,
    private val provinceRepository: ProvinceRepository,
    private val municipalityRepository: MunicipalityRepository
) : StatUpdateService<GasStat>() {

    override fun save(feature: GasStat) {
        gasStatRepository.save(feature)
    }

    override fun buildFeatures(): List<GasStat> {
        val date = Instant.now().truncatedTo(ChronoUnit.DAYS)
        val stats = CommonFuelType.values()
            .flatMap {
                val iPriceStat = gasPriceRepository.findPriceStat(it, null, null)
                listOf(
                    PriceStat(it, iPriceStat.getAvg(), PriceStatType.AVG),
                    PriceStat(it, iPriceStat.getMax(), PriceStatType.MAX),
                    PriceStat(it, iPriceStat.getMin(), PriceStatType.MIN)
                )
            }
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
        return listOf(
            GasStat(
                null, date, stats, provinceMostStations,
                municipalityMostStations, provinceLeastStation,
                municipalityLeastStation
            )
        )
    }

}
