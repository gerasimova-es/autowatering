package com.home.autowatering.config

import io.vertx.core.http.HttpMethod

const val ROOT = "/autowatering"

enum class EndPoint(
    val path: String,
    val method: HttpMethod
) {
    SIGN_UP("$ROOT/signup", HttpMethod.POST),
    POT_LIST("$ROOT/pot/list", HttpMethod.GET),
    POT_INFO("$ROOT/pot/info", HttpMethod.GET),
    POT_SAVE("$ROOT/pot/save", HttpMethod.POST),
    POT_STATISTIC("$ROOT/pot/statistic", HttpMethod.GET),
    POT_STATE_SAVE("$ROOT/pot/state/save", HttpMethod.POST)
}