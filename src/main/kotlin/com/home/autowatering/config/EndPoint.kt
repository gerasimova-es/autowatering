package com.home.autowatering.config

enum class EndPoint(val path: String, val content: String) {
    POT_LIST("/pot/list", "application/json"),
    POT_INFO("/pot/info", "application/json"),
    POT_SAVE("/pot/save", "application/json"),
    POT_STATISTIC("/statistic/:pot/", "application/json"),
    POT_STATE_SAVE("/state/save", "application/json")
}