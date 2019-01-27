package com.home.autowatering.entity.hibernate

import javax.persistence.*


@Entity
@Table(name = "POT")
class JpaPot {
    @Id
    @Column(name = "ID")
    @GeneratedValue
    var id: Long? = null

    @Column(name = "CODE", nullable = false)
    var code: String? = null

    @Column(name = "NAME", nullable = false)
    var name: String? = null

    @Column(name = "HUMIDITY")
    var humidity: Double? = null

    constructor()

    constructor(id: Long? = null, name: String, description: String? = null, humidity: Double? = null) {
        this.id = id
        this.code = name
        this.name = description
        this.humidity = humidity
    }
}