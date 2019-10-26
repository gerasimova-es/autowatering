package com.home.autowatering.validator

interface Validator {
    fun validate(): ValidationResult
}

open class ValidationResult(val type: ValidationType, val message: String = "success")

fun ValidationResult.onError(block: (ValidationResult) -> Unit) {
    if (this.type == ValidationType.ERROR) {
        block.invoke(this)
    }
}

enum class ValidationType {
    ERROR, SUCCESS
}

