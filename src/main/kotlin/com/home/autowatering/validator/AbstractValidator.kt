package com.home.autowatering.validator

abstract class AbstractValidator : Validator {
    var errorHandler: (ValidationResult) -> Unit = {}
    override fun ifError(block: (ValidationResult) -> Unit) : Validator{
        errorHandler = block
        return this
    }
}