package com.home.autowatering

import com.home.autowatering.config.Config
import com.home.autowatering.config.DatabaseConfig
import com.home.autowatering.config.EndPoint
import com.home.autowatering.controller.PotController
import com.home.autowatering.dao.exposed.PotDaoExposed
import com.home.autowatering.dao.jpa.PotStateDaoJpa
import com.home.autowatering.dao.rest.WateringSystemRest
import com.home.autowatering.database.Database
import com.home.autowatering.database.datasource
import com.home.autowatering.service.impl.PotServiceImpl
import com.home.autowatering.service.impl.PotStateServiceImpl
import com.home.autowatering.service.impl.WateringSystemServiceImpl
import com.home.autowatering.util.convert
import com.home.autowatering.util.toISODate
import io.vertx.config.ConfigRetriever
import io.vertx.config.ConfigRetriever.create
import io.vertx.config.ConfigRetriever.getConfigAsFuture
import io.vertx.config.ConfigRetrieverOptions
import io.vertx.config.ConfigStoreOptions
import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.Future.future
import io.vertx.core.Vertx
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class Application : AbstractVerticle() {
    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(Application::class.java)
    }

    override fun start() {
        val steps = getConfigAsFuture(configurer(vertx))
            .compose { config -> prepareDatabase(config.convert<Config>().database!!) }
            .compose { startHttpServer(vertx) }
        steps.result()
    }

    private fun prepareDatabase(config: DatabaseConfig): Future<Any> =
        future<Any> { feature ->
            try {
                val datasource = datasource(config)
                val connection = Database.connect(datasource)
                if (config.fill) {
                    Database.fill(connection)
                }
            } catch (exc: Exception) {
                LOGGER.error("database error: {}", exc.message)
                feature.fail(exc.cause)
            }
        }

    private fun startHttpServer(vertx: Vertx): Future<Any> =
        future<Any> { feature ->
            val server = vertx.createHttpServer()
            val router = Router.router(vertx)

            PotController(
                PotServiceImpl(PotDaoExposed()),
                PotStateServiceImpl(PotStateDaoJpa()),
                WateringSystemServiceImpl(WateringSystemRest())
            ).apply {
                router.routeTo(EndPoint.POT_LIST) { list() }
                    .routeTo(EndPoint.POT_INFO) { context -> info(context.get("code")) }
                    .routeTo(EndPoint.POT_SAVE) { context -> save(context.bodyAsJson.convert()) }
                    .routeTo(EndPoint.POT_STATISTIC) { context ->
                        statistic(
                            context.request().getParam("potCode"),
                            context.request().getParam("dateFrom").toISODate(),
                            context.request().getParam("dateTo").toISODate()
                        )
                    }
                    .routeTo(EndPoint.POT_STATE_SAVE) { context -> saveState(context.bodyAsJson.convert()) }
            }

            server.requestHandler(router)
                .listen(8080) { result ->
                    if (result.succeeded()) {
                        LOGGER.info("Created a server on port ${8080}")
                    } else {
                        LOGGER.error("Unable to create server: \n" + result.cause().message)
                        feature.fail(result.cause())
                    }
                }
        }
}

fun configurer(vertx: Vertx): ConfigRetriever =
    create(
        vertx, ConfigRetrieverOptions()
            .addStore(
                ConfigStoreOptions()
                    .setType("file")
                    .setFormat("json")
                    .setConfig(
                        JsonObject().put(
                            "path",
                            //todo classpath
                            "C:\\work\\repo\\autowatering\\src\\main\\resources\\config\\application.json"
                        )
                    )
            )
    )

fun Router.routeTo(
    endpoint: EndPoint,
    handler: (RoutingContext) -> Any
): Router {
    route(endpoint.path).handler { context ->
        context.response()
            .putHeader("content-type", endpoint.content)
            .setStatusCode(200)
            .end(Json.encodePrettily(handler.invoke(context)))
    }
    return this
}