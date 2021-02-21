package com.home.autowatering.dao.jpa.converter

open class JpaConverter<J, E>(
    private val fromJpa: (jpa: J) -> E,
    private val fromEntity: (entity: E) -> J
) {
    fun fromJpa(jpa: J): E =
        fromJpa.invoke(jpa)


    fun fromEntity(entity: E): J =
        fromEntity.invoke(entity)

    fun fromJpas(jpas: Collection<J>): List<E> =
        jpas.map { fromJpa(it) }

    fun fromEntities(entities: Collection<E>): List<J> =
        entities.map { fromEntity(it) }

}