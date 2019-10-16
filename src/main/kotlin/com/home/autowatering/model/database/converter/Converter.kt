package com.home.autowatering.model.database.converter

open class Converter<J, E>(
    private val fromDB: (jpa: J) -> E,
    private val toDB: (entity: E) -> J
) {
    fun fromDB(jpa: J): E =
        fromDB.invoke(jpa)


    fun fromPojo(pojo: E): J =
        toDB.invoke(pojo)

    fun fromPojos(jpas: Collection<J>): List<E> =
        jpas.map { fromDB(it) }

    fun fromDBs(entities: Collection<E>): List<J> =
        entities.map { fromPojo(it) }

}