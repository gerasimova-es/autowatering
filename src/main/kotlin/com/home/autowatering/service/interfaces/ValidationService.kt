package com.home.autowatering.service.interfaces

import com.home.autowatering.validator.Validator

interface ValidationService {
    fun validate(vararg validators: Validator)
}