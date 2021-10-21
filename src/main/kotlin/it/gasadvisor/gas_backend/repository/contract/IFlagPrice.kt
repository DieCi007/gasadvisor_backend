package it.gasadvisor.gas_backend.repository.contract

interface IFlagPrice {
    fun getFlag(): String
    fun getPrice(): Double
}
