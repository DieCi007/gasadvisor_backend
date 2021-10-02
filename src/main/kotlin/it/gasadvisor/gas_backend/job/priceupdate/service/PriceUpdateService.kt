package it.gasadvisor.gas_backend.job.priceupdate.service

import it.gasadvisor.gas_backend.api.gas.price.service.GasPriceService
import it.gasadvisor.gas_backend.model.GasPrice
import it.gasadvisor.gas_backend.model.GasPriceId
import it.gasadvisor.gas_backend.model.GasStation
import it.gasadvisor.gas_backend.util.Log
import it.gasadvisor.gas_backend.util.TimeUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.text.SimpleDateFormat
import java.util.*

@Service
class PriceUpdateService @Autowired constructor(
    private val service: GasPriceService,
    @Value("\${csv.delimiter}") val delimiter: String
) : GasUpdateService<GasPrice>(delimiter, 5) {
    companion object : Log()

    val dateFormatter: SimpleDateFormat = SimpleDateFormat(TimeUtils.PRICE_CSV_DATE_FORMAT, Locale.ITALY)

    override fun entityFromFields(fields: List<String>): Optional<GasPrice> {
        val stationId = try {
            fields[0].toLong()
        } catch (e: Exception) {
            null
        }
        val price = try {
            fields[2].toDouble()
        } catch (e: Exception) {
            null
        }
        val isSelf = try {
            val value = fields[3].toInt()
            value == 1
        } catch (e: Exception) {
            null
        }

        val date = try {
            TimeUtils.stringToInstant(dateFormatter, fields[4])
        } catch (e: Exception) {
            null
        }
        if (stationId == null || price == null || isSelf == null || date == null) {
            return Optional.empty()
        }

        val gasPrice = GasPrice(
            GasPriceId(
                GasStation(stationId),
                isSelf, date, fields[1]
            ), price
        )
        return Optional.of(gasPrice)
    }

    override fun saveAll(list: List<GasPrice>) {
        return service.saveAll(list)
    }

    override fun handleDirtyLine(line: String) {}
    override fun beforeAll() {}

}
