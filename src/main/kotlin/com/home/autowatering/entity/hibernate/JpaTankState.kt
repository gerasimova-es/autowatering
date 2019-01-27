package com.home.autowatering.entity.hibernate

import java.sql.Date
import javax.persistence.*

@Entity
@Table(name = "TANK_STATE", indexes = [Index(columnList = "DATE", name = "TANK_STATE_DATE")])
class JpaTankState {
    @Id
    @Column(name = "ID")
    @GeneratedValue
    var id: Long? = null

    @Column(name = "CODE", precision = 2, nullable = false)
    var name: String? = null

    @Column(name = "DATE", nullable = false)
    var date: Date? = null

    @Column(name = "VOLUME", precision = 2, nullable = false)
    var volume: Double = 0.0

    @Column(name = "FILLED", precision = 2, nullable = false)
    var filled: Double = 0.0

    constructor()

    constructor(name: String, date: Date, volume: Double, filled: Double) {
        this.name = name
        this.date = date
        this.volume = volume
        this.filled = filled
    }
}