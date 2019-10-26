package com.home.autowatering.model.business.filter

import java.time.temporal.ChronoUnit

enum class SliceType(val unit: ChronoUnit) {
    DAY(ChronoUnit.DAYS),
    HOUR(ChronoUnit.HOURS),
    MINUTE(ChronoUnit.MINUTES),
    WEEK(ChronoUnit.WEEKS)
}