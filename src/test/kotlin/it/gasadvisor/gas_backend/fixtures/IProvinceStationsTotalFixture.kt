package it.gasadvisor.gas_backend.fixtures

import it.gasadvisor.gas_backend.repository.contract.IProvinceStationsTotal

class IProvinceStationsTotalFixture {
    companion object {

        fun getIProvinceStationsTotal(province: String): IProvinceStationsTotal {
            return object : IProvinceStationsTotal {
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
