package com.home.autowatering.entity.hibernate.converter

import java.util.function.Function
import java.util.stream.Collectors

open class JpaConverter<J, E>(
    private val fromJpa: Function<J, E>,
    private val fromEntity: Function<E, J>
) {
    fun fromJpa(jpa: J): E {
        return fromJpa.apply(jpa)
    }

    fun fromEntity(entity: E): J {
        return fromEntity.apply(entity)
    }

    fun fromJpas(jpas: Collection<J>): List<E> {
        return jpas.stream()
            .map { fromJpa(it) }
            .collect(Collectors.toList<E>())
    }

    fun fromEntities(entities: Collection<E>): List<J> {
        return entities.stream()
            .map { fromEntity(it) }
            .collect(Collectors.toList<J>())
    }
}