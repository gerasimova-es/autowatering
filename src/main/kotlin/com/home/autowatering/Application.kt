package com.home.autowatering

import com.home.autowatering.config.Config
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
import io.vertx.config.ConfigRetrieverOptions
import io.vertx.config.ConfigStoreOptions
import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
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

    override fun start(future: Future<Void>) {
        configurer(vertx).getConfig { json ->
            val config = json.result().convert<Config>().apply {
                LOGGER.info("config = $this")
            }
            vertx.executeBlocking<Any>({ blocking ->
                prepareDatabase(config)
                blocking.complete()
            }, {
                startHttpServer(vertx, future, config)
            })
        }
    }

    private fun prepareDatabase(config: Config) {
        val datasource = datasource(config.database!!)
        val connection = Database.connect(datasource)
        if (config.database!!.fill) {
            Database.fill(connection)
        }
    }

    private fun startHttpServer(vertx: Vertx, future: Future<*>, config: Config) {
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
                .routeTo(EndPoint.POT_STATE_SAVE) { context -> saveState(context.bodyAsJson.convert()) }
                .routeTo(EndPoint.POT_STATISTIC) { context ->
                    statistic(
                        context.request().getParam("potCode"),
                        context.request().getParam("dateFrom").toISODate(),
                        context.request().getParam("dateTo").toISODate()
                    )
                }
        }

        server.requestHandler(router)
            .listen(config.port!!) { result ->
                if (result.succeeded()) {
                    LOGGER.info("Created a server on port ${config.port!!}")
                    future.complete()
                } else {
                    LOGGER.error("Unable to create server: \n" + result.cause().message)
                    future.fail(result.cause())
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
                            "C:\\work\\repo\\autowatering\\src\\main\\resources\\config\\config.json"
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