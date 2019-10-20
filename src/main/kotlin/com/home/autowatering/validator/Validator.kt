package com.home.autowatering.validator

interface Validator {
    fun validate(): ValidationResult
    fun ifError(block: (ValidationResult) -> Unit) : Validator
}


