package com.home.autowatering.model

data class Pot(val id: Long?, val name: String?, val states: List<PotState>?) {
    constructor(name: String) : this(null, name, null)
    constructor(id: Long, name: String) : this(id, name, null)
}