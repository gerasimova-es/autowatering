package com.home.autowatering.entity

import javax.persistence.*

@Entity
@Table(name = "POT_STATE")
class PotStateData {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    var id: Long? = null

    @ManyToOne
    @JoinColumn(name = "POT_ID", nullable = false)
    var pot: PotData? = null

    @Column(name = "HUMIDITY", precision = 2)
    var humidity: Double? = null

    constructor()
    constructor(pot: PotData, humidity: Double)
    constructor(id: Long, pot: PotData, humidity: Double?)
}