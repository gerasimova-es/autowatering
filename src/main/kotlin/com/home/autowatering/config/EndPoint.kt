package com.home.autowatering.config

import io.vertx.core.http.HttpMethod

enum class EndPoint(
    val path: String,
    val method: HttpMethod,
    val content: String
) {
    POT_LIST("/pot/list", HttpMethod.GET, "application/json"),
    POT_INFO("/pot/info", HttpMethod.GET, "application/json"),
    POT_SAVE("/pot/save", HttpMethod.POST, "application/json"),
    POT_STATISTIC("/statistic/:pot/", HttpMethod.GET, "application/json"),
    POT_STATE_SAVE("/state/save", HttpMethod.POST, "application/json")
}