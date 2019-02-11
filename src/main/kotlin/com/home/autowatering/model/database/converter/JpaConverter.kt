package com.home.autowatering.model.database.converter

import java.util.function.Function
import java.util.stream.Collectors

open class JpaConverter<J, E>(
    private val fromJpa: Function<J, E>,
    private val fromEntity: Function<E, J>
) {
    fun fromJpa(jpa: J): E =
        fromJpa.apply(jpa)


    fun fromEntity(entity: E): J =
        fromEntity.apply(entity)

    fun fromJpas(jpas: Collection<J>): List<E> =
        jpas.stream()
            .map { fromJpa(it) }
            .collect(Collectors.toList<E>())


    fun fromEntities(entities: Collection<E>): List<J> =
        entities.stream()
            .map { fromEntity(it) }
            .collect(Collectors.toList<J>())

}