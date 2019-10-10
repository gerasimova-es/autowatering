package com.home.autowatering.dao.rest

import com.home.autowatering.dao.interfaces.WateringSystemDao
import com.home.autowatering.model.business.Pot


//@Repository
class WateringSystemRest : WateringSystemDao {
    companion object {
        const val REFRESH_SERVICE = "/pot/settings/save"
    }

    //    @Value("\${watering.url}")
    var url: String? = null

    override fun refresh(pot: Pot) {

//        RestTemplate().exchange(
//            url + REFRESH_SERVICE,
//            HttpMethod.POST,
//            HttpEntity(PotConverter.fromEntity(pot)),
//            object : ParameterizedTypeReference<Response<Any>>() {})
    }
}