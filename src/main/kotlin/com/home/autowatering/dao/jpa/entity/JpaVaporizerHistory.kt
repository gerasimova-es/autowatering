package com.home.autowatering.dao.jpa.entity

import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "VAPORIZER_HISTORY")
class JpaVaporizerHistory {

    @Id
    @Column(name = "ID")
    @GeneratedValue
    var id: Long? = null

    @Column(name = "STATUS", nullable = false)
    var status: Boolean? = null

    @Column(name = "CHECK_DATE")
    var checkDate: Date? = null

    constructor()

    constructor(status: Boolean?, checkDate: Date?) {
        this.status = status
        this.checkDate = checkDate
    }

    constructor(id: Long?, status: Boolean?, checkDate: Date?) {
        this.id = id
        this.status = status
        this.checkDate = checkDate
    }
}