package com.home.autowatering.config

import org.springframework.context.annotation.Configuration


@Configuration
class WateringJooqConfiguration {

//    @Autowired
//    lateinit var dataSource: DataSource
//
//    @Bean
//    fun connectionProvider(): DataSourceConnectionProvider {
//        return DataSourceConnectionProvider(TransactionAwareDataSourceProxy(dataSource))
//    }
//
//    @Bean
//    fun dsl(): DefaultDSLContext =
//        DefaultDSLContext(configuration())
//
//
//    fun configuration(): DefaultConfiguration {
//        val jooqConfiguration = DefaultConfiguration()
//        jooqConfiguration.set(connectionProvider())
//        jooqConfiguration.set(DefaultExecuteListenerProvider(JooqExceptionTranslator()))
//        return jooqConfiguration
//    }

}