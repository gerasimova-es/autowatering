package com.home.autowatering.dto.response

class SendStateResponse : BaseResponse<String> {
    constructor(payload: String) : super(payload)
    constructor(status: ResponseStatus, message: String) : super(status, message)

}