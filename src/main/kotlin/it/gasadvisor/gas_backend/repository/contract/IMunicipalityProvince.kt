package it.gasadvisor.gas_backend.repository.contract

interface IMunicipalityProvince {
    fun getMunicipality(): String
    fun getProvince(): String
}
