package com.home.autowatering.dto

class SendStateResult(val result: String, var message: String? = null) {
    constructor() : this("OK", "Сообщение сохранено успешно") //todo status enum
}