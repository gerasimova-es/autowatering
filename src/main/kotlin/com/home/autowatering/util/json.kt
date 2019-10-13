package com.home.autowatering.util

import io.vertx.core.json.JsonObject


inline fun <reified T : Any> JsonObject.convert(): T =
    mapTo(T::class.java)