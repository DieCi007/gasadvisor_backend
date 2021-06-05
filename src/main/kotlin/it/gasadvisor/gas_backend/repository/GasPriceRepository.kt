package it.gasadvisor.gas_backend.repository

import it.gasadvisor.gas_backend.model.GasPrice
import it.gasadvisor.gas_backend.model.GasPriceId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GasPriceRepository : JpaRepository<GasPrice, GasPriceId>
