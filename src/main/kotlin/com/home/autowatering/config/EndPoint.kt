package com.home.autowatering.config

import io.vertx.core.http.HttpMethod

enum class EndPoint(
    val path: String,
    val method: HttpMethod
) {
    SIGN_UP("/signup", HttpMethod.POST),
    POT_LIST("/pot/list", HttpMethod.GET),
    POT_INFO("/pot/info", HttpMethod.GET),
    POT_SAVE("/pot/save", HttpMethod.POST),
    POT_STATISTIC("/pot/statistic", HttpMethod.GET),
    POT_STATE_SAVE("/pot/state/save", HttpMethod.POST)
}