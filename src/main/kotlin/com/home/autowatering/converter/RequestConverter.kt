package com.home.autowatering.converter

import java.util.function.Function
import java.util.stream.Collectors

/**
 * Converter between DTO and Entities
 */
abstract class RequestConverter<D, E>(
    private val fromDto: Function<D, E>,
    private val fromEntity: Function<E, D>
) {
    /**
     * @param dto DTO entity
     * @return The domain representation - the result of the converting function application on dto entity.
     */
    fun fromDto(dto: D): E {
        return fromDto.apply(dto)
    }

    /**
     * @param entity domain entity
     * @return The DTO representation - the result of the converting function application on domain entity.
     */
    fun fromEntity(entity: E): D {
        return fromEntity.apply(entity)
    }

    /**
     * @param dtos collection of DTO entities
     * @return List of domain representation of provided entities retrieved by
     * mapping each of them with the conversion function
     */
    fun fromDtos(dtos: Collection<D>): List<E> {
        return dtos.stream()
            .map { fromDto(it) }
            .collect(Collectors.toList<E>())
    }

    /**
     * @param entities collection of domain entities
     * @return List of domain representation of provided entities retrieved by
     * mapping each of them with the conversion function
     */
    fun fromEntities(entities: Collection<E>): List<D> {
        return entities.stream()
            .map { fromEntity(it) }
            .collect(Collectors.toList<D>())
    }

}