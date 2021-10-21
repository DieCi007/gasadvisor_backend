package it.gasadvisor.gas_backend.repository.contract

interface IMunicipalityNoStations {
    fun getProvince(): String
    fun getMunicipality(): String
    fun getTotal(): Number
}
