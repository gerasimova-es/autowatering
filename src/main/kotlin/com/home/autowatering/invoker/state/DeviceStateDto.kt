package com.home.autowatering.invoker.state

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
class DeviceStateDto {

    var air: AirState? = null
    var ground: GroundState? = null
    var tanker: TankerStateDto? = null
    var light: LightStateDto? = null
    var vaporizer: VaporizerStateDto? = null
}