package com.home.autowatering.entity

import javax.persistence.*

@Entity
@Table(name = "TANK_STATE")
class TankStateData {
    @Id
    @Column(name = "ID")
    @GeneratedValue
    var id: Long? = null

    @Column(name = "NAME", precision = 2, nullable = false)
    var name: String? = null

    @Column(name = "VOLUME", precision = 2, nullable = false)
    var volume: Double = 0.0

    @Column(name = "FILLED", precision = 2, nullable = false)
    var filled: Double = 0.0

    constructor()

    constructor(name: String, volume: Double, filled: Double) {
        this.name = name
        this.volume = volume
        this.filled = filled
    }

    constructor(id: Long?, name: String, volume: Double, filled: Double) {
        this.id = id
        this.name = name
        this.volume = volume
        this.filled = filled
    }
}