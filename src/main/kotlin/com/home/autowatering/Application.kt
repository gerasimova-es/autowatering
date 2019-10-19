package com.home.autowatering

import com.home.autowatering.config.Config
import com.home.autowatering.config.DatabaseConfig
import com.home.autowatering.config.EndPoint
import com.home.autowatering.controller.PotController
import com.home.autowatering.controller.dto.response.Response
import com.home.autowatering.dao.exposed.PotDaoExposed
import com.home.autowatering.dao.jpa.PotStateDaoJpa
import com.home.autowatering.dao.rest.WateringSystemRest
import com.home.autowatering.database.DatabaseConnector
import com.home.autowatering.database.connect
import com.home.autowatering.database.database
import com.home.autowatering.database.fill
import com.home.autowatering.service.impl.PotServiceImpl
import com.home.autowatering.service.impl.PotStateServiceImpl
import com.home.autowatering.service.impl.WateringSystemServiceImpl
import com.home.autowatering.util.ISODate
import com.home.autowatering.util.convert
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
import io.vertx.ext.web.client.WebClient
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class Application : AbstractVerticle() {
    private var database: DatabaseConnector? = null

    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(Application::class.java)
    }

    override fun start(future: Future<Void>) {
        configurer(vertx).getConfig { json ->
            val config = json.result()
                .convert<Config>()
                .apply { LOGGER.info("config = $this") }

            vertx.executeBlocking<Any>({ blocking ->
                prepareDatabase(config.database!!)
                blocking.complete()
            }, {
                startHttpServer(vertx, future, config)
            })
        }
    }


    override fun stop(stopFuture: Future<Void>) {
        stopDatabase()
        stopFuture.complete()
    }

    private fun prepareDatabase(config: DatabaseConfig) {
        database = database(config)
            .apply {
                start()
                datasource().run {
                    connect()
                    if (config.fill) {
                        fill()
                    }
                }
            }
    }

    private fun stopDatabase() {
        database?.close()
    }

    private fun startHttpServer(vertx: Vertx, future: Future<*>, config: Config) {
        val server = vertx.createHttpServer()
        val router = Router.router(vertx)

        PotController(
            PotServiceImpl(PotDaoExposed()),
            PotStateServiceImpl(PotStateDaoJpa()),
            WateringSystemServiceImpl(
                vertx,
                WateringSystemRest(
                    client = WebClient.create(vertx),
                    board = config.board!!
                )
            )
        ).apply {
            router.routeTo(EndPoint.POT_LIST) { context ->
                list().run { context.response(this) }

            }.routeTo(EndPoint.POT_INFO) { context ->
                info(context.request().getParam("code"))
                    .run { context.response(this) }

            }.routeTo(EndPoint.POT_SAVE) { context ->
                context.request().bodyHandler { body ->
                    save(body.toJsonObject().convert())
                        .run { context.response(this) }
                }
            }.routeTo(EndPoint.POT_STATE_SAVE) { context ->
                context.request().bodyHandler { body ->
                    saveState(body.toJsonObject().convert())
                        .run { context.response(this) }
                }

            }.routeTo(EndPoint.POT_STATISTIC) { context ->
                statistic(
                    context.request().getParam("potCode"),
                    context.request().getParam("dateFrom").ISODate(),
                    context.request().getParam("dateTo").ISODate()
                ).run { context.response(this) }
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
    route(endpoint.method, endpoint.path).handler { context -> handler.invoke(context) }
    return this
}

fun <T> RoutingContext.response(dto: Response<T>? = null) {
    this.response()
        .putHeader("content-type", "application/json")
        .setStatusCode(200)
        .end(dto?.let { Json.encodePrettily(dto) })
}