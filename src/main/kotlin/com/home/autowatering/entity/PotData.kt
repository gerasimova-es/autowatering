package com.home.autowatering.entity

import javax.persistence.*


@Entity
@Table(name = "POT")
class PotData {
    @Id
    @Column(name = "ID")
    @GeneratedValue
    var id: Long? = null

    @Column(name = "NAME")
    var name: String? = null

    @OneToMany(mappedBy = "pot", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    val states: List<PotStateData> = ArrayList<PotStateData>()

    constructor()
    constructor(name: String) {
        this.name = name
    }

    constructor(id: Long, name: String) {
        this.id = id
        this.name = name
    }

}