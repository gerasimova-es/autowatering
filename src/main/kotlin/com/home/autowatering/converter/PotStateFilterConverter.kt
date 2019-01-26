package com.home.autowatering.converter

import com.home.autowatering.dto.PotStateFilterDto
import com.home.autowatering.model.Pot
import com.home.autowatering.model.filter.PotStateFilter
import java.util.function.Function

class PotStateFilterConverter : RequestConverter<PotStateFilterDto, PotStateFilter>(
    Function {
        PotStateFilter.withPot(Pot(name = it.pot))
            .from(it.dateFrom)
            .to(it.dateTo)
            .build()
    },
    Function {
        PotStateFilterDto(it.pot!!.name, it.from, it.to)
    })