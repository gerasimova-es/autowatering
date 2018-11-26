package com.home.autowatering.config

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.context.annotation.Configuration

@Configuration
//@Import(JdbcRepositoriesConfiguration::class, ImportConfiguration::class, RestConfiguration::class)
@EnableAutoConfiguration
open class WateringConfiguration : SpringBootServletInitializer() {

    override fun configure(builder: SpringApplicationBuilder): SpringApplicationBuilder =
        builder.sources(WateringConfiguration::class.java)
}