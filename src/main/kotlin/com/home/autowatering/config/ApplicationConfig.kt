package com.home.autowatering.config

import com.home.autowatering.dao.repository.LightingSettingsRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener
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