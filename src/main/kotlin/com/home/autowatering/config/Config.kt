package com.home.autowatering.config

data class Config(
    var board: Board? = null,
    var database: Database? = null
)

data class Board(
    var url: String? = null
)

data class Database(
    var fill: Boolean = false,
    var jdbc: JDBC? = null
)

data class JDBC(
    var driver: String? = null,
    var url: String? = null,
    var user: String? = null,
    var password: String? = null
)