package it.gasadvisor.gas_backend.repository.contract

import it.gasadvisor.gas_backend.api.gas.station.contract.IGetAllStationsResponse

interface INearestStation: IGetAllStationsResponse {
    fun getAddress(): String
    fun getFlag(): String
    fun getOwner(): String
    fun getDistance(): Long
}
