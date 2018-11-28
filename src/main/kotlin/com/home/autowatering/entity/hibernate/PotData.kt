package com.home.autowatering.entity.hibernate

import javax.persistence.*


@Entity
@Table(name = "POT")
class PotData {
    @Id
    @Column(name = "ID")
    @GeneratedValue
    private var id: Long? = null

    @Column(name = "NAME", nullable = false)
    private var name: String? = null

    @OneToMany(mappedBy = "pot", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    private val states: List<PotStateData> = ArrayList<PotStateData>()

    constructor()

    constructor(name: String) {
        this.name = name
    }

}