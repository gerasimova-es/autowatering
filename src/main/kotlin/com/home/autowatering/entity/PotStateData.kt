package com.home.autowatering.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class PotStateData {
    @Id
    @GeneratedValue
    var id: Long? = null
    var pot: PotData? = null
    var humidity: Double? = null

    constructor()
    constructor(pot: PotData, humidity: Double)
    constructor(id: Long, pot: PotData, humidity: Double?)
}