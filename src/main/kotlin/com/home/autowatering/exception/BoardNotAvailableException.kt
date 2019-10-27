package com.home.autowatering.exception

import com.home.autowatering.controller.dto.response.StatusType.BOARD_NOT_AVAILABLE

class BoardNotAvailableException(message: String) : AutowateringException(BOARD_NOT_AVAILABLE, message)