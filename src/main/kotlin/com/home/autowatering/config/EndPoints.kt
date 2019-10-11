package com.home.autowatering.config

enum class EndPoints(val path: String, val content: String) {
    POT_LIST("/pot/list", "application/json")
}