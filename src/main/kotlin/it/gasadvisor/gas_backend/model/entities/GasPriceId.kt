package it.gasadvisor.gas_backend.model.entities

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable
import java.time.Instant
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.FetchType
import javax.persistence.ManyToOne

@Embeddable
class GasPriceId(
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = ["prices"], allowSetters = true)
    var gasStation: GasStation,
    var isSelf: Boolean,
    var readDate: Instant,
    @Column(length = 200)
    var description: String
) : Serializable
