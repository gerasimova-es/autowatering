package com.home.autowatering.validator

import com.home.autowatering.model.business.filter.SliceType
import com.home.autowatering.validator.ValidationType.ERROR
import com.home.autowatering.validator.ValidationType.SUCCESS
import java.time.ZonedDateTime

class PeriodValidator(
    private val dateFrom: ZonedDateTime?,
    private val dateTo: ZonedDateTime?,
    private val slice: SliceType?
) : Validator {
    override fun validate(): ValidationResult =
        when {
            dateFrom == null || dateTo == null -> ValidationResult(SUCCESS, "success")
            dateFrom > dateTo ->
                ValidationResult(ERROR, "begin date cannot be less then end date")
            slice?.unit?.between(dateFrom, dateTo) == 0L ->
                ValidationResult(ERROR, "it is not  possible use slice \"$slice\" unit between begin date $dateFrom and end date $dateTo")
            else -> ValidationResult(SUCCESS)
        }
}