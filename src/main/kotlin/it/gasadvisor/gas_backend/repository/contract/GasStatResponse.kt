package it.gasadvisor.gas_backend.repository.contract

import it.gasadvisor.gas_backend.model.entities.Municipality
import it.gasadvisor.gas_backend.model.entities.PriceStat
import it.gasadvisor.gas_backend.model.entities.Province
import java.time.Instant

data class GasStatResponse(
    var date: Instant,
    var prices: List<PriceStat>,
    var mostStationsProvince: Province?,
    var mostStationsMunicipality: Municipality?,
    var leastStationsProvince: Province?,
    var leastStationsMunicipality: Municipality?
)
