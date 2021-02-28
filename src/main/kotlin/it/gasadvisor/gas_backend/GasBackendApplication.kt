package it.gasadvisor.gas_backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GasBackendApplication

fun main(args: Array<String>) {
	runApplication<GasBackendApplication>(*args)
}
