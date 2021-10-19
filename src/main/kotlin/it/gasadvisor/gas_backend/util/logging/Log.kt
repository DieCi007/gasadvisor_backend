package it.gasadvisor.gas_backend.util.logging

import org.slf4j.LoggerFactory
import org.slf4j.Logger

abstract class Log {
    val log: Logger = LoggerFactory.getLogger(this.javaClass)
}
