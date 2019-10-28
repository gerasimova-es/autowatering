package com.home.autowatering.config

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class  Config @JsonCreator constructor(
    @field:JsonProperty("port")
    val port: Int,
    @field:JsonProperty("root")
    val root: String,
    @field:JsonProperty("board")
    val board: BoardConfig,
    @field:JsonProperty("database")
    val database: DatabaseConfig
)

data class BoardConfig @JsonCreator constructor(
    @field:JsonProperty("url")
    val url: String,
    @field:JsonProperty("timeout")
    val timeout: Long
)

data class DatabaseConfig @JsonCreator constructor(
    @field:JsonProperty("mode")
    val mode: DatabaseMode? = null,
    @field:JsonProperty("fill")
    val fill: Boolean = false,
    @field:JsonProperty("datasource")
    val datasource: DatasourceConfig? = null
)

enum class DatabaseMode {
    EMBEDDED, REMOTE
}

data class DatasourceConfig @JsonCreator constructor(
    @field:JsonProperty("driver")
    val driver: String? = null,
    @field:JsonProperty("url")
    val url: String? = null,
    @field:JsonProperty("user")
    val user: String? = null,
    @field:JsonProperty("password")
    val password: String? = null
)