package it.gasadvisor.gas_backend.job.app_update.processor

import it.gasadvisor.gas_backend.job.app_update.service.ProvinceStatUpdateService
import it.gasadvisor.gas_backend.util.Log
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import java.lang.Exception

@Component
@Qualifier("provinceStatUpdateProcessor")
class ProvinceStatUpdateProcessor @Autowired constructor(
    private val provinceStatUpdateService: ProvinceStatUpdateService
) : GasUpdateProcessor {
    companion object : Log()

    override fun update() {
        try {
            provinceStatUpdateService.update()
        } catch (e: Exception) {
            log.info("Exception during province stat update")
            log.error(e.message, e)
        }
    }
}
