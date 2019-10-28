package com.home.autowatering.exception

import com.home.autowatering.controller.dto.response.StatusType

class PotValidateException(message: String) :
    AutowateringException(StatusType.INCORRECT_POT_DATA, message)