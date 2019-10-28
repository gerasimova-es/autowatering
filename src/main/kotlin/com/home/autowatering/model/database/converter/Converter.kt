package com.home.autowatering.model.database.converter

open class Converter<J, E>(
    private val fromDB: (db: J) -> E,
    private val toDB: (entity: E) -> J
) {
    fun fromDB(db: J): E =
        fromDB.invoke(db)


    fun fromPojo(pojo: E): J =
        toDB.invoke(pojo)

    fun fromPojos(dbs: Collection<J>): List<E> =
        dbs.map { fromDB(it) }

    fun fromDBs(entities: Collection<E>): List<J> =
        entities.map { fromPojo(it) }

}