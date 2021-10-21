package it.gasadvisor.gas_backend.repository.contract

interface IProvinceNoStations {
    fun getProvince(): String
    fun getTotal(): Number
}
