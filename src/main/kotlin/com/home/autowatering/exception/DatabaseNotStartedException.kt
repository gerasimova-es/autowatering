package com.home.autowatering.exception

import com.home.autowatering.controller.dto.response.StatusType

class DatabaseNotStartedException : AutowateringException(
    StatusType.INTERNAL_ERROR, "database not started"
)