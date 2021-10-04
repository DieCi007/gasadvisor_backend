package it.gasadvisor.gas_backend.api.gas.station.service

import it.gasadvisor.gas_backend.model.entities.GasStation
import org.springframework.data.jpa.domain.Specification
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root

class StationSpecification {
    companion object {
        fun filter(query: String): Specification<GasStation> {
            return Specification { root: Root<GasStation>, _: CriteriaQuery<*>, cb: CriteriaBuilder ->
                val filterPredicates = cb.or(
                    cb.like(cb.concat(root.get("id"), ""), "%" + query.toLowerCase() + "%"),
                    cb.like(cb.lower(root.get("owner")), "%" + query.toLowerCase() + "%"),
                    cb.like(cb.lower(root.get("flag")), "%" + query.toLowerCase() + "%"),
                    cb.like(cb.lower(root.get("type")), "%" + query.toLowerCase() + "%"),
                    cb.like(cb.lower(root.get("name")), "%" + query.toLowerCase() + "%"),
                    cb.like(cb.lower(root.get("address")), "%" + query.toLowerCase() + "%"),
                    cb.like(cb.lower(root.get("municipality")), "%" + query.toLowerCase() + "%"),
                    cb.like(cb.lower(root.get("province")), "%" + query.toLowerCase() + "%")
                )
                cb.and(filterPredicates)
            }
        }
    }

}
