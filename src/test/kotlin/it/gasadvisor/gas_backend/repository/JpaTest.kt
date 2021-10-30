package it.gasadvisor.gas_backend.repository

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.TestPropertySource
import org.springframework.transaction.annotation.EnableTransactionManagement
import java.lang.annotation.ElementType
import java.lang.annotation.Inherited
import java.lang.annotation.Target

@Retention(AnnotationRetention.RUNTIME)
@Target(ElementType.TYPE)
@MustBeDocumented
@Inherited
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("classpath:application-test.properties")
@EnableTransactionManagement(proxyTargetClass = true)
annotation class JpaTest
