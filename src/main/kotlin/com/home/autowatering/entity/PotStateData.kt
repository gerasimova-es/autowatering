package com.home.autowatering.entity

import java.sql.Date
import javax.persistence.*

@Entity
@Table(name = "POT_STATE")
class PotStateData {
    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false)
    var id: Long? = null

    @Column(name = "DATE", nullable = false)
    var date: Date? = null

    @ManyToOne
    @JoinColumn(name = "POT_ID", nullable = false)
    var pot: PotData? = null

    @Column(name = "HUMIDITY", precision = 2, nullable = false)
    var humidity: Double? = null

    constructor()

    constructor(pot: PotData, date: Date, humidity: Double) {
        this.pot = pot
        this.date = date
        this.humidity = humidity
    }

    constructor(id: Long, pot: PotData, date: Date, humidity: Double?) {
        this.id = id
        this.pot = pot
        this.date = date
        this.humidity = humidity
    }
}