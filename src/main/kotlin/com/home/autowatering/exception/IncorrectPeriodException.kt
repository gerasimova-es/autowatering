package com.home.autowatering.exception

import com.home.autowatering.controller.dto.response.StatusType

class IncorrectPeriodException(message: String) : AutowateringException(StatusType.INCORRECT_PERIOD_DATES, message)