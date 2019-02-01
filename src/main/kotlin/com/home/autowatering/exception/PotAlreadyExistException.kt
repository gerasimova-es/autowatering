package com.home.autowatering.exception

class PotAlreadyExistException(code: String) : RuntimeException("pot with code = $code already exists")