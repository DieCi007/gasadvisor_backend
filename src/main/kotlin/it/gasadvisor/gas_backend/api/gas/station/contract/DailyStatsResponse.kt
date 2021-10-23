package it.gasadvisor.gas_backend.api.gas.station.contract

import it.gasadvisor.gas_backend.repository.contract.IMunicipalityNoStations
import it.gasadvisor.gas_backend.repository.contract.IProvinceNoStations

data class LocationNoStations(
    val provinceMostStations: List<IProvinceNoStations>,
    val provinceLeastStations: List<IProvinceNoStations>,
    val municipalityMostStations: List<IMunicipalityNoStations>,
    val municipalityLeastStations: List<IMunicipalityNoStations>,
)
