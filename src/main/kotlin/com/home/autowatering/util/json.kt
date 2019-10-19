package com.home.autowatering.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import io.vertx.core.json.JsonObject

private val mapper: ObjectMapper = jacksonObjectMapper() // or different mapper for other format
    .registerModule(ParameterNamesModule())
    .registerModule(Jdk8Module())
    .registerModule(JavaTimeModule())

fun objectMapper() = mapper

inline fun <reified T : Any?> JsonObject?.convert(): T {
    if (this == null || this.isEmpty) {
        throw RuntimeException("object is null")
    }
    //todo toString?
    return objectMapper().readValue(this.toString(), T::class.java)
}