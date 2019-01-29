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

    constructor()

    constructor(id: Long? = null, name: String, description: String? = null) {
        this.id = id
        this.code = name
        this.name = description
    }
}