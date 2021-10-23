package it.gasadvisor.gas_backend.repository.contract

import java.time.Instant

interface IDatePrice {
    fun getPrice(): Double
    fun getDate(): Instant
}
