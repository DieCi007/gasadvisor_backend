package it.gasadvisor.gas_backend.fixtures

import it.gasadvisor.gas_backend.repository.contract.IMunicipalityStationsTotal

class IMunicipalityStationsTotalFixture {
    companion object {
        fun getIMunicipalityStationsTotal(municipality: String, province: String): IMunicipalityStationsTotal {
            return object : IMunicipalityStationsTotal {
                override fun getMunicipality(): String {
                    return municipality
                }

                override fun getProvince(): String {
                    return province
                }

                override fun getTotal(): Long {
                    return 1
                }
            }
        }
    }
}
