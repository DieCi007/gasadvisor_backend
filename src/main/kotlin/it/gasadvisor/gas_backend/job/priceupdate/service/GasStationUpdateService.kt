package it.gasadvisor.gas_backend.job.priceupdate.service

import it.gasadvisor.gas_backend.api.gas.station.service.GasStationService
import it.gasadvisor.gas_backend.model.GasStation
import it.gasadvisor.gas_backend.util.Log
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class GasStationUpdateService @Autowired constructor(
    private val service: GasStationService,
    @Value("\${csv.delimiter}") val delimiter: String
) : GasUpdateService<GasStation>(delimiter, 10) {
    companion object : Log()

    override fun entityFromFields(fields: List<String>): Optional<GasStation> {
        val id = try {
            fields[0].toLong()
        } catch (e: Exception) {
            null
        }
        val latitude = try {
            fields[8].toDouble()
        } catch (e: Exception) {
            null
        }
        val longitude = try {
            fields[9].toDouble()
        } catch (e: Exception) {
            null
        }
        if (id == null || latitude == null || longitude == null) {
            return Optional.empty()
        }
        val station = GasStation(
            id, fields[1], fields[2],
            fields[3], fields[4], fields[5], fields[6],
            fields[7], latitude, longitude
        )
        return Optional.of(station)
    }

    override fun saveAll(list: List<GasStation>) {
        service.saveAll(list)
    }

}
