package com.home.autowatering.api.converter

import java.util.stream.Collectors

/**
 * JpaConverter between DTO and Entities
 */
open class RequestConverter<D, E>(
    private val fromDto: (dto: D) -> E,
    private val fromEntity: (entity: E) -> D
) {
    /**
     * @param dto DTO entity
     * @return The domain representation - the result of the converting function application on dto entity.
     */
    fun fromDto(dto: D): E =
        fromDto.invoke(dto)

    /**
     * @param entity domain entity
     * @return The DTO representation - the result of the converting function application on domain entity.
     */
    fun fromEntity(entity: E): D =
        fromEntity.invoke(entity)

    /**
     * @param dtos collection of DTO entities
     * @return List of domain representation of provided entities retrieved by
     * mapping each of them with the conversion function
     */
    fun fromDtos(dtos: Collection<D>): List<E> =
        dtos.stream()
            .map { fromDto(it) }
            .collect(Collectors.toList<E>())

    /**
     * @param entities collection of domain entities
     * @return List of domain representation of provided entities retrieved by
     * mapping each of them with the conversion function
     */
    fun fromEntities(entities: Collection<E>): List<D> =
        entities.stream()
            .map { fromEntity(it) }
            .collect(Collectors.toList<D>())

}