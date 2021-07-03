package it.gasadvisor.gas_backend.repository.contract

interface IMunicipalityStationsTotal {
    fun getMunicipality(): String
    fun getTotal(): Long
}
