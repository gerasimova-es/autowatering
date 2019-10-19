package com.home.autowatering.exception

import com.home.autowatering.controller.dto.response.StatusType

class SavingException(cause: Throwable) :
    AutowateringException(StatusType.INTERNAL_ERROR, cause.message)