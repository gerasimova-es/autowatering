package com.home.autowatering.util

import io.vertx.core.json.JsonObject


inline fun <reified T : Any?> JsonObject?.convert(): T {
    if (this == null || this.isEmpty) {
        throw RuntimeException("object is null")
    }
    return mapTo(T::class.java)
}