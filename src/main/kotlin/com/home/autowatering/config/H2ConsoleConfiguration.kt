package com.home.autowatering.config

import org.h2.server.web.WebServlet
import org.springframework.boot.web.servlet.ServletRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class H2ConsoleConfiguration {

    @Bean
    internal fun h2servletRegistration(): ServletRegistrationBean<*> {
        val registration = ServletRegistrationBean(WebServlet())
        registration.addUrlMappings("/console/*")
        return registration
    }

}