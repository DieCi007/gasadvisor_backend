package it.gasadvisor.gas_backend.job.priceupdate.processor

import it.gasadvisor.gas_backend.job.priceupdate.service.MunicipalityUpdateService
import it.gasadvisor.gas_backend.job.priceupdate.service.ProvinceUpdateService
import it.gasadvisor.gas_backend.util.Log
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
@Qualifier("provinceUpdateProcessor")
class ProvinceUpdateProcessor @Autowired constructor(
    private val service: ProvinceUpdateService,
    private val municipalityUpdateService: MunicipalityUpdateService
) : GasUpdateProcessor {
    companion object : Log()

    override fun update() {
        try {
            service.update()
            municipalityUpdateService.update()
        } catch (e: Exception) {
            log.info("Exception during province update")
            log.error(e.message, e)
        }
    }
}
