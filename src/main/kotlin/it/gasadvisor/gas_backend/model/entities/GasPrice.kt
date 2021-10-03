package it.gasadvisor.gas_backend.model.entities

import javax.persistence.*

@Entity
@Table(name = "gas_price")
class GasPrice(
    @EmbeddedId
    var id: GasPriceId,
    var price: Double,
    ) {
    override fun toString(): String {
        return "GasPrice(date=${id.readDate}, description=${id.description}, price=$price)"
    }
}

