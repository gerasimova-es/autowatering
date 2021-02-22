package com.home.autowatering.dao.jpa.entity

import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "AIR_CONDITION")
class JpaAirCondition{
    @Id
    @Column(name = "ID")
    @GeneratedValue
    var id: Long? = null

    @Column(name = "HUMIDITY")
    var humidity: Int? = null

    @Column(name = "TEMPERATURE")
    var temperature: Int? = null

    @Column(name = "CHECK_DATE")
    var checkDate: Date? = null

    constructor()

    constructor(humidity: Int?, temperature: Int?, checkDate: Date?) {
        this.humidity = humidity
        this.temperature = temperature
        this.checkDate = checkDate
    }

    constructor(id: Long?, humidity: Int?, temperature: Int?, checkDate: Date?) {
        this.id = id
        this.humidity = humidity
        this.temperature = temperature
        this.checkDate = checkDate
    }
}