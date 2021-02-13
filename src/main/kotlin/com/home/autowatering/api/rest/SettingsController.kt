package com.home.autowatering.api.rest;

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/settings")
class SettingsController {

    @GetMapping("/info")
    fun info(): {

    }

}
