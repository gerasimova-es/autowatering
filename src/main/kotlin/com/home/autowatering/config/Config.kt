package com.home.autowatering.config

data class Config(
    var jdbc: JDBC? = null
)

data class JDBC(
    var driver: String? = null,
    var url: String? = null,
    var user: String? = null,
    var password: String? = null
)