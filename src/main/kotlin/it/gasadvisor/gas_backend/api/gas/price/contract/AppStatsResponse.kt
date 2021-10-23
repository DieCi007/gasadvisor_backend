package it.gasadvisor.gas_backend.api.gas.price.contract

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class AppStatsResponse(
    val mostStationsMunicipality: MunicipalityProvince?,
    val mostStationsProvince: String?,
    val leastStationsMunicipality: MunicipalityProvince?,
    val leastStationsProvince: String?,
    val priceStats: List<PriceStatResponse>
)
