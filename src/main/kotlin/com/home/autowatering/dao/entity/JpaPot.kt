package com.home.autowatering.dao.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table
import javax.validation.constraints.Max
import javax.validation.constraints.Min


@Entity
@Table(name = "POT")
class JpaPot {
    @Id
    @Column(name = "ID")
    @GeneratedValue
    var id: Long? = null

    @Column(name = "CODE", nullable = false, unique = true)
    var code: String? = null

    @Column(name = "NAME", nullable = false)
    var name: String? = null

    @Column(name = "MIN_HUMIDITY", nullable = false)
    @Min(0)
    @Max(1024)
    var minHumidity: Int? = null

    @Column(name = "CHECK_INTERVAL", nullable = false)
    @Min(1)
    @Max(1440)
    var checkInterval: Int? = null

    @Column(name = "WATERING_DURATION", nullable = false)
    @Min(1)
    @Max(10)
    var wateringDuration: Int? = null

    constructor()

    constructor(
        id: Long? = null,
        code: String,
        name: String?,
        minHumidity: Int?,
        checkInterval: Int?,
        wateringDuration: Int?
    ) {
        this.id = id
        this.code = code
        this.name = name
        this.minHumidity = minHumidity
        this.checkInterval = checkInterval
        this.wateringDuration = wateringDuration
    }
}