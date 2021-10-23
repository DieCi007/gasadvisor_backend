package it.gasadvisor.gas_backend.api.gas.price.contract

data class AppStatsResponse(
    val mostStationsMunicipality: MunicipalityProvince?,
    val mostStationsProvince: String?,
    val leastStationsMunicipality: MunicipalityProvince?,
    val leastStationsProvince: String?,
    val priceStats: List<PriceStatResponse>
)
