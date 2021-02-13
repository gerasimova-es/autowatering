package com.home.autowatering.dao.entity

import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Index
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.persistence.Temporal
import javax.persistence.TemporalType

@Entity
@Table(
    name = "POT_STATE",
    indexes = [Index(columnList = "DATE, POT_ID", name = "POT_STATE_DATE")]
)
class JpaPotState {
    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false)
    var id: Long? = null

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATE", nullable = false)
    var date: Date? = null

    @ManyToOne
    @JoinColumn(name = "POT_ID", nullable = false)
    var pot: JpaPot? = null

    @Column(name = "HUMIDITY", nullable = false)
    var humidity: Int? = null

    @Column(name = "WATERING")
    var watering: Boolean? = null

    constructor()

    constructor(id: Long? = null, pot: JpaPot? = null, date: Date, humidity: Int, watering: Boolean?) {
        this.id = id
        this.pot = pot
        this.date = date
        this.humidity = humidity
        this.watering = watering
    }
}