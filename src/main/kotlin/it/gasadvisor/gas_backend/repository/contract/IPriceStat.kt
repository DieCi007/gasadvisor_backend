package it.gasadvisor.gas_backend.repository.contract

interface IPriceStat {
    fun getAvg(): Double
    fun getMin(): Double
    fun getMax(): Double
}
