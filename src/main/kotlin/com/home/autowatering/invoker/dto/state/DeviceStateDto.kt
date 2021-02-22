package com.home.autowatering.invoker.dto.state

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
class DeviceStateDto {

    var air: AirStateDto? = null
    var ground: GroundStateDto? = null
    var tanker: TankerStateDto? = null
    var light: LightStateDto? = null
    var vaporizer: VaporizerStateDto? = null

    constructor()

    constructor(
        air: AirStateDto?,
        ground: GroundStateDto?,
        tanker: TankerStateDto?,
        light: LightStateDto?,
        vaporizer: VaporizerStateDto?
    ) {
        this.air = air
        this.ground = ground
        this.tanker = tanker
        this.light = light
        this.vaporizer = vaporizer
    }

}