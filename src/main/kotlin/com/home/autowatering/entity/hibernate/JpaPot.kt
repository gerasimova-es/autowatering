package com.home.autowatering.entity.hibernate

import javax.persistence.*
import javax.validation.constraints.Max
import javax.validation.constraints.Min


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

    @Column(name = "MIN_HUMIDITY", nullable = false)
    @Min(0)
    @Max(1024)
    var minHumidity: Int? = null

    constructor()

    constructor(id: Long? = null, name: String, description: String?, minHumidity: Int?) {
        this.id = id
        this.code = name
        this.name = description
        this.minHumidity = minHumidity
    }
}