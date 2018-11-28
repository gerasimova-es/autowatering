package com.home.autowatering.entity.hibernate

import java.sql.Date
import javax.persistence.*

@Entity
@Table(name = "POT_STATE")
class PotStateData {
    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false)
    private var id: Long? = null

    @Column(name = "DATE", nullable = false)
    private var date: Date? = null

    @ManyToOne
    @JoinColumn(name = "POT_ID", nullable = false)
    private var pot: PotData? = null

    @Column(name = "HUMIDITY", precision = 2, nullable = false)
    private var humidity: Double? = null

    constructor()

    constructor(pot: PotData, date: Date, humidity: Double) {
        this.pot = pot
        this.date = date
        this.humidity = humidity
    }
}