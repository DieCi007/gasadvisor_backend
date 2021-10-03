package it.gasadvisor.gas_backend.repository

import it.gasadvisor.gas_backend.model.entities.Province
import it.gasadvisor.gas_backend.repository.contract.IProvinceStationsTotal
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ProvinceRepository : JpaRepository<Province, Long> {
    fun findByName(name: String): Optional<Province>

    @Query(
        "select gs.province as province, count(*) as total from gas_station gs " +
                "group by gs.province order by total desc limit 1", nativeQuery = true
    )
    fun findOneWithMostStations(): IProvinceStationsTotal

    @Query(
        "select gs.province as province, count(*) as total from gas_station gs " +
                "group by gs.province order by total asc limit 1", nativeQuery = true
    )
    fun findOneWithLeastStations(): IProvinceStationsTotal

}
