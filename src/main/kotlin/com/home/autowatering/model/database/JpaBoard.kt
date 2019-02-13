package com.home.autowatering.model.database

//@Entity
//@Table(name = "BOARD")
class JpaBoard {
    //    @Id
//    @Column(name = "ID")
//    @GeneratedValue
    var id: Long? = null

    //    @Column(name="NAME")
    var name: String? = null

    //    @Column(name="IP")
    var ip: String? = null

    //    @Column(name="ACTIVE")
    var active: Boolean? = null
//
//    @OneToMany
//    @JoinColumn(name="BOARD_ID")
//    var pots: Set<JpaPot> = setOf()
}