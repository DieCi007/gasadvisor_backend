package it.gasadvisor.gas_backend.api.gas.station.contract

interface IGetAllStationsResponse {
    fun getId(): Long
    fun getLatitude(): Double
    fun getLongitude(): Double
}
