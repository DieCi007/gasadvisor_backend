package it.gasadvisor.gas_backend.model

import javax.persistence.*

@Entity
@Table(name = "gas_price")
class GasPrice(
    @EmbeddedId
    var id: GasPriceId,

    var price: Double,

)

