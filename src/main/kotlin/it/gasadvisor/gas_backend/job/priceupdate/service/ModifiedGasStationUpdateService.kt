package it.gasadvisor.gas_backend.job.priceupdate.service

import it.gasadvisor.gas_backend.model.GasStation
import it.gasadvisor.gas_backend.repository.GasStationRepository
import it.gasadvisor.gas_backend.repository.ModifiedGasStationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ModifiedGasStationUpdateService @Autowired constructor(
    private val stationRepo: GasStationRepository,
    private val modifiedStationRepo: ModifiedGasStationRepository
) : StatUpdateService<GasStation>() {
    override fun save(feature: GasStation): GasStation {
        return stationRepo.save(feature)
    }

    override fun buildFeatures(): List<GasStation> {
        val modifiedStations = modifiedStationRepo.findAll()
        println(modifiedStations[0].toString())
        val ret =  modifiedStations.map { mStation ->
            val station = mStation.station
            val address = mStation.address
            if (address != null) {
                station.address = address
            }
            val owner = mStation.owner
            if (owner != null) {
                station.owner = owner
            }
            val flag = mStation.flag
            if (flag != null) {
                station.flag = flag
            }
            val type = mStation.type
            if (type != null) {
                station.type = type
            }
            val name = mStation.name
            if (name != null) {
                station.name = name
            }
            val municipality = mStation.municipality
            if (municipality != null) {
                station.municipality = municipality
            }
            val province = mStation.province
            if (province != null) {
                station.province = province
            }
            val latitude = mStation.latitude
            if (latitude != null) {
                station.latitude = latitude
            }
            val longitude = mStation.longitude
            if (longitude != null) {
                station.longitude = longitude
            }
            return@map station
        }
        println(ret[0].toString())
        return ret
    }
}
