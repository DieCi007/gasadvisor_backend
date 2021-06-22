package it.gasadvisor.gas_backend.api.gas.station.contract

interface GasStationAnalyticsResponse {
    fun getId(): Long?
    fun getOwner(): String?
    fun getFlag(): String?
    fun getType(): String?
    fun getName(): String?
    fun getAddress(): String?
    fun getMunicipality(): String?
    fun getProvince(): String?
    fun getLatitude(): Double?
    fun getLongitude(): Double?

}
