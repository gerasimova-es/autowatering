package com.home.autowatering.dao.jpa.entity

import java.time.LocalTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table
import javax.validation.constraints.Max
import javax.validation.constraints.Min

@Entity
@Table(name = "WHISTLING")
class JpaWhistling {

    @Id
    @Column(name = "ID")
    @GeneratedValue
    var id: Long? = null

    @Column(name = "ENABLED", nullable = false)
    var enabled: Boolean? = null

    @Column(name = "DURATION", nullable = false)
    @Min(100)
    @Max(1000)
    var duration: Int? = null

    @Column(name = "START_TIME", nullable = false, columnDefinition = "TIME")
    var startTime: LocalTime? = null

    @Column(name = "STOP_TIME", nullable = false, columnDefinition = "TIME")
    var stopTime: LocalTime? = null

    constructor()

    constructor(enabled: Boolean?, duration: Int?, startTime: LocalTime?, stopTime: LocalTime?) {
        this.enabled = enabled
        this.duration = duration
        this.startTime = startTime
        this.stopTime = stopTime
    }

    constructor(id: Long?, enabled: Boolean?, duration: Int?, startTime: LocalTime?, stopTime: LocalTime?) {
        this.id = id
        this.enabled = enabled
        this.duration = duration
        this.startTime = startTime
        this.stopTime = stopTime
    }

}