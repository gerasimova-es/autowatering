package com.home.autowatering.entity.hibernate

import javax.persistence.*


@Entity
@Table(name = "POT")
class JpaPot {
    @Id
    @Column(name = "ID")
    @GeneratedValue
    var id: Long? = null

    @Column(name = "NAME", nullable = false)
    var name: String? = null

    @Column(name = "DESCRIPTION", nullable = false)
    var description: String? = null

    constructor()

    constructor(id: Long? = null, name: String, description: String? = null) {
        this.id = id
        this.name = name
        this.description = description
    }
}