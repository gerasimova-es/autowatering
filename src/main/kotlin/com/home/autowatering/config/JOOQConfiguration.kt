package com.home.autowatering.config

import org.h2.jdbcx.JdbcDataSource
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class JOOQConfiguration {
//
//    @Value("#{jdbc.driver}")
//    var driver: String? = "o"

    @Value("#{jdbc.url}")
    var url: String? = null

    @Value("#{jdbc.user}")
    var user: String? = null

    @Value("#{jdbc.password}")
    var password: String? = null

    @Bean
    fun dataSource(): DataSource {
        val source = JdbcDataSource()
        source.setUrl(url)
        source.setUser(user)
        source.setPassword(password)
        return source
    }
}