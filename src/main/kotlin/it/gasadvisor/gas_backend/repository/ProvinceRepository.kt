package it.gasadvisor.gas_backend.repository

import it.gasadvisor.gas_backend.model.Province
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ProvinceRepository: JpaRepository<Province, Long> {
    fun findByName(name: String): Optional<Province>
}
