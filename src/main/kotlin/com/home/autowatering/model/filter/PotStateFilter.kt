package com.home.autowatering.model.filter

import com.home.autowatering.model.Pot
import java.util.*

data class PotStateFilter(
    var pot: Pot,
    var from: Date? = null,
    var to: Date? = null
)