package com.home.autowatering.controller.converter

import com.home.autowatering.controller.dto.response.Response
import java.util.stream.Collectors

/**
 * JpaConverter between DTO and Entities
 */
open class RequestConverter<D, E>(
    private val fromDto: (dto: D) -> E,
    private val fromEntity: (entity: E) -> D
) {
    /**
     * @param dto DTO database
     * @return The domain representation - the result of the converting function application on dto database.
     */
    fun fromDto(dto: D): E =
        fromDto.invoke(dto)

    /**
     * @param entity domain database
     * @return The DTO representation - the result of the converting function application on domain database.
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

    fun response(entity: E): Response<D> =
        Response(fromEntity(entity))

    fun response(entities: List<E>): Response<List<D>> =
        Response(fromEntities(entities))

}