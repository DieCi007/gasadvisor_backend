package it.gasadvisor.gas_backend.util

import org.springframework.security.core.context.SecurityContextHolder

class SecurityUtils {
    companion object {
        fun getAuthenticatedUsername(): String {
            return SecurityContextHolder.getContext().authentication.name;
        }
    }
}
