package com.home.autowatering.model.business.filter

import com.home.autowatering.model.business.Pot
import java.time.ZonedDateTime

data class PotStateFilter(
    var pot: Pot,
    var from: ZonedDateTime? = null,
    var to: ZonedDateTime? = null,
    var limit: Int? = null
)