package it.gasadvisor.gas_backend.fixtures

import it.gasadvisor.gas_backend.repository.contract.IPriceStat

class IPriceStatFixture {
    companion object {
        fun getIPriceStat(): IPriceStat {
            return object : IPriceStat {
                override fun getAvg(): Double {
                    return 2.50
                }

                override fun getMin(): Double {
                    return 2.00
                }

                override fun getMax(): Double {
                    return 3.00
                }
            }
        }
    }
}
