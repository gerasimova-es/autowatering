package com.home.autowatering.dto

import java.util.*

class PotStateFilterDto(val pot: String, var dateFrom: Date? = null, var dateTo: Date? = null)