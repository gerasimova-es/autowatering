package com.home.autowatering.config

data class Config(
    var port: Int? = null,
    var board: BoardConfig? = null,
    var database: DatabaseConfig? = null
)

data class BoardConfig(
    val url: String,
    val timeout: Long
)

data class DatabaseConfig(
    val mode: DatabaseMode? = null,
    val fill: Boolean = false,
    val pool: ConnectionPool? = null,
    val datasource: DatasourceConfig? = null
)

enum class DatabaseMode {
    EMBEDDED, REMOTE
}

data class ConnectionPool(
    val size: Int? = null
)

data class DatasourceConfig(
    val driver: String? = null,
    val url: String? = null,
    val user: String? = null,
    val password: String? = null
)