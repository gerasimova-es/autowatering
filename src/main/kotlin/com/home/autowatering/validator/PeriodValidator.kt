package com.home.autowatering.validator

import com.home.autowatering.model.business.filter.SliceType
import java.time.ZonedDateTime

class PeriodValidator(val dateFrom: ZonedDateTime?, val dateTo: ZonedDateTime?, val slice: SliceType?) : AbstractValidator() {
    override fun validate(): ValidationResult {
        return ValidationResult(ValidationType.SUCCESS)
    }
}