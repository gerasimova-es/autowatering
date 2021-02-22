package com.home.autowatering.dao.jpa.entity

import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "TANKER_HISTORY")
class JpaTankerHistory {

    @Id
    @Column(name = "ID")
    @GeneratedValue
    var id: Long? = null

    @Column(name = "IS_FULL", nullable = false)
    var full: Boolean? = null

    @Column(name = "CHECK_DATE", nullable = false)
    var checkDate: Date? = null

    constructor()

    constructor(full: Boolean?, checkDate: Date?) {
        this.full = full
        this.checkDate = checkDate
    }

    constructor(id: Long?, full: Boolean?, checkDate: Date?) {
        this.id = id
        this.full = full
        this.checkDate = checkDate
    }

}