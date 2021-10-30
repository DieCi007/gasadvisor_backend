package it.gasadvisor.gas_backend.repository.contract

interface INearestStation {
    fun getAddress(): String
    fun getFlag(): String
    fun getOwner(): String
    fun getDistance(): Long
}
