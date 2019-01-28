package com.home.autowatering.exception

class PotNotFoundException : RuntimeException {
    constructor(id: Long) : super("pot not found by id = $id")
    constructor(code: String) : super("pot not found by code = $code")
}