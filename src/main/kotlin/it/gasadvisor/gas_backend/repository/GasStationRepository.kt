package it.gasadvisor.gas_backend.repository

import it.gasadvisor.gas_backend.api.gas.station.contract.GetAllStationsResponse
import it.gasadvisor.gas_backend.api.gas.station.contract.GetStationDataResponse
import it.gasadvisor.gas_backend.api.gas.station.contract.IGetAllStationsResponse
import it.gasadvisor.gas_backend.model.entities.GasStation
import it.gasadvisor.gas_backend.model.enums.CommonFuelType
import it.gasadvisor.gas_backend.repository.contract.IMunicipalityNoStations
import it.gasadvisor.gas_backend.repository.contract.IMunicipalityProvince
import it.gasadvisor.gas_backend.repository.contract.INearestStation
import it.gasadvisor.gas_backend.repository.contract.IProvinceNoStations
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface GasStationRepository : JpaRepository<GasStation, Long>, JpaSpecificationExecutor<GasStation> {

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

    @Query("select distinct gs.province from GasStation gs")
    fun findAllProvinces(): List<String>

    @Query(
        "select distinct gs.province from GasStation gs where gs.province not in " +
                "(select p.name from Province p)"
    )
    fun findNotSavedProvinces(): List<String>

    @Query(
        "select distinct gs.municipality as municipality, gs.province as province from GasStation gs where gs.municipality not in " +
                "(select m.name from Municipality m)"
    )
    fun findNotSavedMunicipalities(): List<IMunicipalityProvince>

    @Query(
        "select gs.province as province, count(*) as total from gas_station gs group by gs.province order by total desc limit 4",
        nativeQuery = true
    )
    fun findProvinceWithMostStations(): List<IProvinceNoStations>

    @Query(
        "select gs.province as province, count(*) as total from gas_station gs group by gs.province order by total limit 4",
        nativeQuery = true
    )
    fun findProvinceWithLeastStations(): List<IProvinceNoStations>

    @Query(
        "select gs.province as province, gs.municipality as municipality,  count(*) as total from gas_station gs " +
                "group by province, municipality order by total desc limit 4",
        nativeQuery = true
    )
    fun findMunicipalityWithMostStations(): List<IMunicipalityNoStations>

    @Query(
        "select gs.province as province, gs.municipality as municipality, count(*) as total from gas_station gs " +
                "group by province, municipality order by total limit 4",
        nativeQuery = true
    )
    fun findMunicipalityWithLeastStations(): List<IMunicipalityNoStations>

    @Query(
        "select gs.address as address, gs.flag as flag, gs.owner as owner, gs.id as id, gs.latitude as latitude, gs.longitude as longitude, " +
                "st_distance_sphere(point(gs.latitude, gs.longitude), point(:lat, :lon)) " +
                "as distance from gas_station gs where gs.status = 'ACTIVE' order by distance limit :limit",
        nativeQuery = true
    )
    fun findNearestStations(
        @Param("lat") lat: Double,
        @Param("lon") lon: Double,
        @Param("limit") limit: Int
    ): List<INearestStation>

    @Query(
        "select distinct gs.id as id, gs.latitude as latitude, gs.longitude as longitude from gas_price gp, gas_station gs where " +
                "gp.gas_station_id = gs.id and " +
                "gp.read_date = (select max(p.read_date) from gas_price p where p.gas_station_id = gp.gas_station_id) and " +
                "(:province is null or gs.province = :province) and " +
                "((:province is null or :municipality is null) or gs.municipality = :municipality) and " +
                "(:fuel is null or gp.description in (select c.name from explicit_fuel_type  c where c.common_type = :#{#fuel?.name()})) and " +
                "((:distance is null or :lat is null or :lon is null) or " +
                "st_distance_sphere(point(gs.latitude, gs.longitude), point(:lat, :lon)) <= :distance)",
        nativeQuery = true
    )
    fun filter(
        @Param("province") province: String?,
        @Param("municipality") municipality: String?,
        @Param("fuel") fuel: CommonFuelType?,
        @Param("distance") distanceMeters: Long?,
        @Param("lat") lat: Double?,
        @Param("lon") lon: Double?,
    ): List<IGetAllStationsResponse>

}
