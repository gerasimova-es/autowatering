package com.home.autowatering.dao.jpa.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table
import javax.validation.constraints.Max
import javax.validation.constraints.Min

@Entity
@Table(name = "VAPORIZER")
class JpaVaporizer {

    @Id
    @Column(name = "ID")
    @GeneratedValue
    var id: Long? = null

    @Column(name = "ENABLED", nullable = false)
    var enabled: Boolean? = null

    @Column(name = "MIN_HUMIDITY", nullable = false)
    @Min(0)
    @Max(1024)
    var minHumidity: Int? = null

    @Column(name = "CHECK_INTERVAL", nullable = false)
    @Min(1)
    @Max(1440)
    var checkInterval: Int? = null

    constructor()

    constructor(enabled: Boolean?, minHumidity: Int?, checkInterval: Int?) {
        this.enabled = enabled
        this.minHumidity = minHumidity
        this.checkInterval = checkInterval
    }

    constructor(id: Long?, enabled: Boolean?, minHumidity: Int?, checkInterval: Int?) {
        this.id = id
        this.enabled = enabled
        this.minHumidity = minHumidity
        this.checkInterval = checkInterval
    }

}