package com.home.autowatering.model.business

import java.net.Inet4Address

data class Board(
    val id: Long? = null,
    val name: String? = null,
    val ip: Inet4Address,
    val active: Boolean? = false,
    val pots: Set<Pot> = setOf()
)