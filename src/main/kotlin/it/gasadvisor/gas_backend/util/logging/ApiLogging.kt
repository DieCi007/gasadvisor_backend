package it.gasadvisor.gas_backend.util.logging

import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Before
import org.springframework.stereotype.Component

@Component
@Aspect
class ApiLogging {
    companion object : Log()

    @Before("execution(* it.gasadvisor.gas_backend.api..*(..))")
    fun log(point: JoinPoint) {
        log.info(
            "called method {} in {} passing arguments {}",
            point.signature.name,
            point.target.toString(),
            point.args
        )
    }

}
