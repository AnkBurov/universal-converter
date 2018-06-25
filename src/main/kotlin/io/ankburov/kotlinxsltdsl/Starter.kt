package io.ankburov.kotlinxsltdsl

import io.ankburov.kotlinxsltdsl.config.MainConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import


@SpringBootApplication
@Import(MainConfiguration::class)
class Starter

fun main(args: Array<String>) {

    runApplication<Starter>(*args)
}
