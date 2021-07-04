package it.gasadvisor.gas_backend.repository.contract

interface IMunicipalityStationsTotal {
    fun getMunicipality(): String
    fun getProvince(): String
    fun getTotal(): Long
}
