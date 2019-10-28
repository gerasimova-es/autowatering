package com.home.autowatering.validator

import com.home.autowatering.controller.dto.PotDto

class PotRequestValidator(private val request: PotDto) : Validator {
    override fun validate(): ValidationResult {
        return when{
            request.code.isEmpty() -> ValidationResult(ValidationType.ERROR, "field code cannot be empty")
            request.name.isNullOrEmpty() -> ValidationResult(ValidationType.ERROR, "field name cannot be empty")
            //todo the rest
            else -> ValidationResult(ValidationType.SUCCESS)
        }
    }
}