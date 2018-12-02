package com.home.autowatering.dto.response

abstract class BaseResponse<T> {
    var status: ResponseStatus? = ResponseStatus.SUCCESS
    var paload: T? = null
}