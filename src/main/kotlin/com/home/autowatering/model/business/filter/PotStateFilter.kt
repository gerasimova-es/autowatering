package com.home.autowatering.model.business.filter

import com.home.autowatering.model.business.Pot
import java.util.*

data class PotStateFilter(
    var pot: Pot,
    var from: Date? = null,
    var to: Date? = null,
    var limit: Int? = null
)