package com.home.autowatering.exception

import com.home.autowatering.controller.dto.response.StatusType

class DatabaseAlreadyStartedException : AutowateringException(
    StatusType.INTERNAL_ERROR, "database already started"
)