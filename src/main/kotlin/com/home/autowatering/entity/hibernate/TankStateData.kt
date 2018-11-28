package com.home.autowatering.entity.hibernate

import java.sql.Date
import javax.persistence.*

@Entity
@Table(name = "TANK_STATE")
class TankStateData {
    @Id
    @Column(name = "ID")
    @GeneratedValue
    private var id: Long? = null

    @Column(name = "NAME", precision = 2, nullable = false)
    private var name: String? = null

    @Column(name = "DATE", nullable = false)
    private var date: Date? = null

    @Column(name = "VOLUME", precision = 2, nullable = false)
    private var volume: Double = 0.0

    @Column(name = "FILLED", precision = 2, nullable = false)
    private var filled: Double = 0.0

    constructor()

    constructor(name: String, date: Date, volume: Double, filled: Double) {
        this.name = name
        this.date = date
        this.volume = volume
        this.filled = filled
    }
}