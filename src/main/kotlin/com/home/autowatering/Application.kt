package com.home.autowatering

import com.home.autowatering.config.Config
import com.home.autowatering.config.DatabaseConnector
import com.home.autowatering.config.EndPoints
import com.home.autowatering.controller.PotController
import com.home.autowatering.dao.exposed.PotDaoExposed
import com.home.autowatering.dao.jpa.PotStateDaoJpa
import com.home.autowatering.dao.rest.WateringSystemRest
import com.home.autowatering.exception.ConfigLoadException
import com.home.autowatering.exception.ConfigNotFoundException
import com.home.autowatering.service.impl.PotServiceImpl
import com.home.autowatering.service.impl.PotStateServiceImpl
import com.home.autowatering.service.impl.WateringSystemServiceImpl
import io.vertx.config.ConfigRetriever
import io.vertx.config.ConfigRetrieverOptions
import io.vertx.config.ConfigStoreOptions
import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Router
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class Application : AbstractVerticle() {
    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(Application::class.java)
    }

    override fun start(future: Future<Void>) {
        super.start(future)
        ConfigRetriever.create(
            vertx, ConfigRetrieverOptions()
                .addStore(
                    ConfigStoreOptions()
                        .setType("file")
                        .setFormat("json")
                        .setConfig(
                            JsonObject().put(
                                "path",
                                "C:\\work\\repo\\autowatering\\src\\main\\resources\\config\\application.json"
                            )
                        )
                )
        ).getConfig { initial ->
            if (initial.failed()) {
                throw ConfigLoadException(initial.cause().message)
            }
            val config = initial.result()?.mapTo(Config::class.java)
                ?: throw ConfigNotFoundException()


            LOGGER.info("loaded properties = $config")

            //todo async
            DatabaseConnector(
                config.jdbc ?: throw ConfigNotFoundException("jdbc")
            ).connect()


            val potController = PotController(
                potService = PotServiceImpl(
                    potDao = PotDaoExposed()
                ),
                potStateService = PotStateServiceImpl(
                    stateDao = PotStateDaoJpa()
                ),
                wateringSystemService = WateringSystemServiceImpl(
                    wateringSystemDao = WateringSystemRest()
                )
            )

            val server = vertx.createHttpServer()
            val router = Router.router(vertx)

            router.route(EndPoints.POT_LIST.path).handler { context ->
                context.response()
                    .putHeader("content-type", EndPoints.POT_LIST.content)
                    .setStatusCode(200)
                    .end(Json.encodePrettily(potController.list()))
            }

            //assign server
            server.requestHandler(router)
                .listen(config().getInteger("http.port", 8080)) { res ->
                    if (res.succeeded()) {
                        LOGGER.info("Created a server on port 8080")
                    } else {
                        LOGGER.error("Unable to create server: \n" + res.cause().localizedMessage)
                        future.fail(res.cause())
                    }
                }
        }
    }
}
