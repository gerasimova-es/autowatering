package com.home.autowatering.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class PotData {
    @Id
    @GeneratedValue
    var id: Long? = null
    var name: String? = null

    constructor()
    constructor(name: String)
    constructor(id: Long, name: String)

}