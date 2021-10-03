package it.gasadvisor.gas_backend.job.app_update.service

import it.gasadvisor.gas_backend.api.gas.explicit_fuel.service.ExplicitFuelService
import it.gasadvisor.gas_backend.api.gas.min_price.service.MinPriceService
import it.gasadvisor.gas_backend.api.gas.price.service.GasPriceService
import it.gasadvisor.gas_backend.model.entities.*
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
    private val explicitFuelService: ExplicitFuelService,
    private val minPriceService: MinPriceService,
    @Value("\${csv.delimiter}") val delimiter: String
) : GasUpdateService<GasPrice>(delimiter, 5) {
    companion object : Log()

    lateinit var minPrices: List<FuelMinPrice>
    lateinit var fuelTypes: List<ExplicitFuelType>

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

        val type = fields[1]
        val explicitFuel = fuelTypes.find { it.name == type }
        if (explicitFuel?.commonType != null) {
            val minPrice = minPrices.find { it.type == explicitFuel.commonType }
            if (minPrice?.minPrice != null && minPrice.minPrice > price) {
                return Optional.empty()
            }
        }

        val gasPrice = GasPrice(
            GasPriceId(
                GasStation(stationId),
                isSelf, date, type
            ), price
        )
        return Optional.of(gasPrice)
    }

    override fun saveAll(list: List<GasPrice>) {
        return service.saveAll(list)
    }

    override fun handleDirtyLine(line: String) {}
    override fun beforeAll() {
        minPrices = minPriceService.getAll()
        fuelTypes = explicitFuelService.getAll()
    }

}
