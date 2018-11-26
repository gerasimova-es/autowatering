package com.home.autowatering.entity

import javax.persistence.*

@Entity
@Table(name = "TANK")
class TankData {
    @Id
    @Column(name = "ID")
    @GeneratedValue
    var id: Long? = null

    @Column(name = "VOLUME", precision = 2)
    var volume: Double = 0.0

    @Column(name = "FILLED", precision = 2)
    var filled: Double = 0.0

    constructor()
    constructor(volume: Double)
    constructor(id: Long, volume: Double, filled: Double)
}