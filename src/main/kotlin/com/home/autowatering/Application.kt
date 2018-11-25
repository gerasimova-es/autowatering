package com.home.autowatering

import com.home.autowatering.config.WateringConfiguration
import org.springframework.boot.SpringApplication

fun main(args: Array<String>) {
    SpringApplication.run(WateringConfiguration::class.java, *args)
}