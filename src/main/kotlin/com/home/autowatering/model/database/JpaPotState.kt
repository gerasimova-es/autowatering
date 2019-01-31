package com.home.autowatering.model.database

import java.util.*
import javax.persistence.*

@Entity
@Table(
    name = "POT_STATE",
    indexes = [Index(columnList = "DATE, POT_ID", name = "POT_STATE_DATE")]
)
class JpaPotState {
    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false)
    var id: Long? = null

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATE", nullable = false)
    var date: Date? = null

    @ManyToOne
    @JoinColumn(name = "POT_ID", nullable = false)
    var pot: JpaPot? = null

    @Column(name = "HUMIDITY", precision = 2, nullable = false)
    var humidity: Double? = null

    @Column(name = "WATERING")
    var watering: Boolean? = null

    constructor()

    constructor(id: Long? = null, pot: JpaPot? = null, date: Date, humidity: Double, watering: Boolean?) {
        this.id = id
        this.pot = pot
        this.date = date
        this.humidity = humidity
        this.watering = watering
    }
}