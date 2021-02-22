package com.home.autowatering.dao.jpa.entity

import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "GROUND_CONDITION")
class JpaGroundCondition {
    @Id
    @Column(name = "ID")
    @GeneratedValue
    var id: Long? = null

    @Column(name = "HUMIDITY")
    var humidity: Int? = null

    @Column(name = "CHECK_DATE")
    var checkDate: Date? = null

    constructor()

    constructor(humidity: Int?, checkDate: Date?) {
        this.humidity = humidity
        this.checkDate = checkDate
    }

    constructor(id: Long?, humidity: Int?, checkDate: Date?) {
        this.id = id
        this.humidity = humidity
        this.checkDate = checkDate
    }

}