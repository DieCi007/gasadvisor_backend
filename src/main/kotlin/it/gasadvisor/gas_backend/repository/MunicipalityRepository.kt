package it.gasadvisor.gas_backend.repository

import it.gasadvisor.gas_backend.model.Municipality
import it.gasadvisor.gas_backend.repository.contract.IMunicipalityStationsTotal
import it.gasadvisor.gas_backend.repository.contract.IProvinceStationsTotal
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface MunicipalityRepository : JpaRepository<Municipality, Long> {
    @Query(
        "select m from Municipality m where m.name = :municipality " +
                "and m.province.name = :province"
    )
    fun findByNameAndProvince(
        @Param("municipality") municipality: String,
        @Param("province") province: String
    ): Optional<Municipality>

    @Query(
        "select gs.municipality as municipality, count(*) as total from gas_station gs " +
                "where gs.municipality in (select gs1.municipality from gas_station gs1 where " +
                "gs1.province = gs.province) " +
                "group by gs.municipality, gs.province order by total desc limit 1", nativeQuery = true
    )
    fun findOneWithMostStations(): IMunicipalityStationsTotal

    @Query(
        "select gs.municipality as municipality, count(*) as total from gas_station gs " +
                "where gs.municipality in (select gs1.municipality from gas_station gs1 where " +
                "gs1.province = gs.province) " +
                "group by gs.municipality, gs.province order by total asc limit 1", nativeQuery = true
    )
    fun findOneWithLeastStations(): IMunicipalityStationsTotal


}

