package com.housetainer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HousetainerApplication

fun main(args: Array<String>) {
    runApplication<HousetainerApplication>(*args)
}
