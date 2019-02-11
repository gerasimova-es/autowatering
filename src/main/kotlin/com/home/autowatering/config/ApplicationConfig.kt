package com.home.autowatering.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.filter.CommonsRequestLoggingFilter


@Configuration
class ApplicationConfig {

    @Bean
    fun requestLoggingFilter(): CommonsRequestLoggingFilter =
        CommonsRequestLoggingFilter()
            .also {
                it.setIncludeClientInfo(true)
                it.setIncludeQueryString(true)
                it.setIncludePayload(true)
                it.setIncludeHeaders(true)
            }

//    @Bean
//    internal fun h2servletRegistration(): ServletRegistrationBean<*> {
//        val registration = ServletRegistrationBean(WebServlet( )
//        registration.addUrlMappings("/console/*")
//        return registration
//    }
}