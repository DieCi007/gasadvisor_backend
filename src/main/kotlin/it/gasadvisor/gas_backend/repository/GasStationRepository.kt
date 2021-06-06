package it.gasadvisor.gas_backend.repository

import it.gasadvisor.gas_backend.api.gas.station.contract.GasStationResponse
import it.gasadvisor.gas_backend.api.gas.station.contract.GetAllStationsResponse
import it.gasadvisor.gas_backend.api.gas.station.contract.GetStationDataResponse
import it.gasadvisor.gas_backend.model.GasStation
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface GasStationRepository : JpaRepository<GasStation, Long> {

    @Query(
        "select new it.gasadvisor.gas_backend.api.gas.station.contract.GetAllStationsResponse(g.id, g.latitude, g.longitude) " +
                "from GasStation g"
    )
    fun findAllLocations(): List<GetAllStationsResponse>

    @Query(
        "select new it.gasadvisor.gas_backend.api.gas.station.contract.GetStationDataResponse(g.owner, g.flag, " +
                "g.type, g.name, g.address, g.municipality, g.province) " +
                "from GasStation g where g.id = :stationId"
    )
    fun getStationData(@Param("stationId") stationId: Long): Optional<GetStationDataResponse>

    @Query("select gs.id as id, gs.owner as owner, gs.flag as flag, gs.type as type, gs.name as name, gs.address as address, " +
            "gs.municipality as municipality, gs.province as province, gs.latitude as latitude, gs.longitude as longitude from GasStation gs")
    fun findAllPaginated(page: PageRequest): Page<GasStationResponse>
}
