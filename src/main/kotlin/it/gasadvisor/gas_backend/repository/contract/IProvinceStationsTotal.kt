package it.gasadvisor.gas_backend.repository.contract

interface IProvinceStationsTotal {
    fun getProvince(): String
    fun getTotal(): Long
}
