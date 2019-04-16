package com.home.autowatering.controller

import com.home.autowatering.controller.converter.TankStateConverter
import com.home.autowatering.controller.dto.TankStateDto
import com.home.autowatering.controller.dto.response.Response
import com.home.autowatering.service.interfaces.TankStateService

//@RestController
//@RequestMapping("/tank")
class TankStateController(val tankStateService: TankStateService) : AbstractController() {

    //    @PostMapping("/state/save")
    fun save(
//    @RequestBody
        request: TankStateDto
    ): Response<TankStateDto> {
        val result = tankStateService.save(TankStateConverter.fromDto(request))
        return TankStateConverter.response(result)
    }
}