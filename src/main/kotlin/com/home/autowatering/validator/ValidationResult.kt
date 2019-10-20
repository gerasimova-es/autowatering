package com.home.autowatering.validator

class ValidationResult(val type: ValidationType) {
    fun message(): String = "" //todo
}

enum class ValidationType {
    ERROR, SUCCESS
}