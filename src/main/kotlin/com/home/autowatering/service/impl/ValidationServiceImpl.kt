package com.home.autowatering.service.impl

import com.home.autowatering.service.interfaces.ValidationService
import com.home.autowatering.validator.ValidationType
import com.home.autowatering.validator.Validator

class ValidationServiceImpl : ValidationService {
    override fun validate(vararg validators: Validator) {
        validators.forEach { validator ->
            val result = validator.validate()
            if(result.type == ValidationType.ERROR){
               // validator.ifError {  }
            }
        }
    }
}