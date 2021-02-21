package com.home.autowatering.dao.jpa.entity

import java.time.LocalTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "LIGHTING_SETTINGS")
class JpaLightingSettings {

    @Id
    @Column(name = "ID")
    @GeneratedValue
    var id: Long? = null

    @Column(name = "ENABLED", nullable = false)
    var enabled: Boolean? = null

    @Column(name = "START_TIME", nullable = false, columnDefinition = "TIME")
    var startTime: LocalTime? = null

    @Column(name = "STOP_TIME", nullable = false, columnDefinition = "TIME")
    var stopTime: LocalTime? = null

    constructor()

    constructor(id: Long?, enabled: Boolean?, startTime: LocalTime?, stopTime: LocalTime?) {
        this.id = id
        this.enabled = enabled
        this.startTime = startTime
        this.stopTime = stopTime
    }
}
