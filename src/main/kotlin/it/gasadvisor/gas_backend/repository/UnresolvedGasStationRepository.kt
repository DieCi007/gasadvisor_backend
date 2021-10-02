package it.gasadvisor.gas_backend.repository

import it.gasadvisor.gas_backend.model.UnresolvedGasStation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UnresolvedGasStationRepository : JpaRepository<UnresolvedGasStation, String> {
    @Query("select s from UnresolvedGasStation s where s.isResolved = :isResolved")
    fun findByResolved(@Param("isResolved") isResolved: Boolean): List<UnresolvedGasStation>
}
