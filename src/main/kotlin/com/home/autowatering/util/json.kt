package com.home.autowatering.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import io.vertx.core.json.JsonObject

val mapper: ObjectMapper by lazy {
    jacksonObjectMapper()
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        .registerModule(ParameterNamesModule())
        .registerModule(Jdk8Module())
        .registerModule(JavaTimeModule())
}

inline fun <reified T : Any?> JsonObject?.convert(): T {
    if (this == null || this.isEmpty) {
        throw RuntimeException("object is null")
    }
    return mapper.readValue(this.toString(), T::class.java)
}

//vertx JsonObject uses own mapper instance without additional options
// which is needed for ZonedDateTime serialization
fun Any.toJson() =
    JsonObject(
        mapper.writeValueAsString(this)
    )
