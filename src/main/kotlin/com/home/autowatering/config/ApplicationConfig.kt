package com.home.autowatering.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.filter.CommonsRequestLoggingFilter


@Configuration
class ApplicationConfig {

    @Bean
    fun requestLoggingFilter(): CommonsRequestLoggingFilter {
        val loggingFilter = CommonsRequestLoggingFilter()
        loggingFilter.setIncludeClientInfo(false)
        loggingFilter.setIncludeQueryString(false)
        loggingFilter.setIncludePayload(true)
        loggingFilter.setIncludeHeaders(false)
        return loggingFilter
    }

//    @Bean
//    internal fun h2servletRegistration(): ServletRegistrationBean<*> {
//        val registration = ServletRegistrationBean(WebServlet())
//        registration.addUrlMappings("/console/*")
//        return registration
//    }
}