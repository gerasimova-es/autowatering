package com.home.autowatering.entity.hibernate

import javax.persistence.*


@Entity
@Table(name = "POT")
class PotData {
    @Id
    @Column(name = "ID")
    @GeneratedValue
    var id: Long? = null

    @Column(name = "NAME", nullable = false)
    var name: String? = null

    @OneToMany(mappedBy = "pot", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    val states: List<PotStateData> = ArrayList<PotStateData>()

    constructor()

    constructor(name: String) {
        this.name = name
    }

}