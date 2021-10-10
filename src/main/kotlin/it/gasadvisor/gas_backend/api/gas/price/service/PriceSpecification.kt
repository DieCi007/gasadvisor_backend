package it.gasadvisor.gas_backend.api.gas.price.service

import it.gasadvisor.gas_backend.model.entities.GasPrice
import it.gasadvisor.gas_backend.model.entities.GasPriceId
import it.gasadvisor.gas_backend.model.entities.GasStation
import org.springframework.data.jpa.domain.Specification
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root

class PriceSpecification {
    companion object {
        fun filter(query: String): Specification<GasPrice> {
            return Specification { root: Root<GasPrice>, _: CriteriaQuery<*>, cb: CriteriaBuilder ->
                val filterPredicates = cb.or(
                    cb.like(
                        cb.concat(root.get<GasPriceId>("id").get<GasStation>("gasStation").get("id"), ""),
                        "%" + query.toLowerCase() + "%"
                    ),
                    cb.like(cb.lower(root.get<GasPriceId>("id").get("description")), "%" + query.toLowerCase() + "%"),
                    cb.like(cb.concat(root.get("price"), ""), "%" + query.toLowerCase() + "%"),
                )
                cb.and(filterPredicates)
            }
        }
    }
}
