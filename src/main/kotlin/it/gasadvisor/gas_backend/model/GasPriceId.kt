package it.gasadvisor.gas_backend.model

import java.io.Serializable
import java.time.Instant
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.ManyToOne

@Embeddable
class GasPriceId(
    @ManyToOne
    var gasStation: GasStation,
    var isSelf: Boolean,
    var readDate: Instant,
    @Column(length = 200)
    var description: String
) : Serializable
