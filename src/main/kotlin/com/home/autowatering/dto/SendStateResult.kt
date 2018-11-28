package com.home.autowatering.dto

class SendStateResult(private val result: String, private var message: String? = null) {
    constructor() : this("OK", "Сообщение сохранено успешно") //todo status enum
}