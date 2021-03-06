package com.home.autowatering

import com.home.autowatering.config.Config
import com.home.autowatering.config.DatabaseConfig
import com.home.autowatering.config.EndPoint
import com.home.autowatering.controller.PotController
import com.home.autowatering.controller.UserController
import com.home.autowatering.controller.dto.response.Response
import com.home.autowatering.controller.dto.response.StatusType
import com.home.autowatering.dao.exposed.PotDaoExposed
import com.home.autowatering.dao.exposed.PotStateDaoExposed
import com.home.autowatering.dao.rest.BoardSettingsRest
import com.home.autowatering.database.DatabaseConnector
import com.home.autowatering.database.connect
import com.home.autowatering.database.database
import com.home.autowatering.database.fill
import com.home.autowatering.exception.AutowateringException
import com.home.autowatering.model.business.filter.SliceType
import com.home.autowatering.service.impl.PotServiceImpl
import com.home.autowatering.service.impl.PotStateServiceImpl
import com.home.autowatering.service.impl.WateringSystemServiceImpl
import com.home.autowatering.util.convert
import com.home.autowatering.util.toISODate
import com.home.autowatering.util.toJson
import io.vertx.config.ConfigRetriever
import io.vertx.config.ConfigRetriever.create
import io.vertx.config.ConfigRetrieverOptions
import io.vertx.config.ConfigStoreOptions
import io.vertx.core.AbstractVerticle
import io.vertx.core.AsyncResult
import io.vertx.core.Future
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory

const val CONFIG_PATH = "/config/config.json"

class Application : AbstractVerticle() {
    var database: DatabaseConnector? = null

    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(Application::class.java)
    }

    override fun start(future: Future<Void>) {
        configurer(vertx).getConfig { json ->
            val config = json.result()
                .convert<Config>()
                .also { LOGGER.info("config = $this") }

            vertx.executeBlocking<Any>({ blocking ->
                prepareDatabase(config.database)
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
        //todo fix - not stop correctly sometimes
        database?.close()
    }

    private fun startHttpServer(vertx: Vertx, future: Future<*>, config: Config) {
        val server = vertx.createHttpServer()
        val router = Router.router(vertx)

        UserController().apply {
            router.routeTo(config.root, EndPoint.SIGN_UP) { context ->
                context.request().bodyHandler { body ->
                    signup(body.toJsonObject().convert())
                        .run { context.response(this) }
                }
            }
        }

        PotController(
            PotServiceImpl(PotDaoExposed()),
            PotStateServiceImpl(PotStateDaoExposed()),
            WateringSystemServiceImpl(
                BoardSettingsRest(
                    board = config.board
                )
            )
        ).apply {
            router.routeTo(config.root, EndPoint.POT_LIST) { context ->
                list().setHandler { result ->
                    context.response(result)
                }
            }.routeTo(config.root, EndPoint.POT_INFO) { context ->
                info(context.param("code"))
                    .run { context.response(this) }

            }.routeTo(config.root, EndPoint.POT_SAVE) { context ->
                context.request().bodyHandler { body ->
                    save(body.toJsonObject().convert())
                        .run { context.response(this) }
                }
            }.routeTo(config.root, EndPoint.POT_STATE_SAVE) { context ->
                context.request().bodyHandler { body ->
                    saveState(body.toJsonObject().convert())
                        .run { context.response(this) }
                }

            }.routeTo(config.root, EndPoint.POT_STATISTIC) { context ->
                statistic(
                    context.param("code"),
                    context.param("dateFrom").toISODate(),
                    context.param("dateTo").toISODate(),
                    context.param("slice")?.let { SliceType.valueOf(it) }
                ).run { context.response(this) }
            }
        }

        server.requestHandler(router)
            .listen(config.port) { result ->
                if (result.succeeded()) {
                    LOGGER.info("Created a server on port ${config.port}")
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
                            "path", Application::class.java.getResource(CONFIG_PATH).path
                        )
                    )
            )
    )

fun RoutingContext.param(name: String) =
    this.request().getParam(name)

fun Router.routeTo(
    root: String,
    endpoint: EndPoint,
    handler: (RoutingContext) -> Any
): Router {
    route(endpoint.method, root + endpoint.path)
        .handler { context -> handler.invoke(context) }
    //todo add request logging?
    return this
}

fun <T> RoutingContext.response(result: AsyncResult<Response<T>>) {
    val response: Response<T> = when {
        result.failed() ->
            when (val error = result.cause()) {
                is AutowateringException -> {
                    Application.LOGGER.debug("error: ", error)
                    Response(error.status, error.message)
                }
                else -> {
                    Application.LOGGER.error("internal error:", error)
                    Response(
                        StatusType.INTERNAL_ERROR,
                        if (error.message == null) error.javaClass.name else error.message!!
                    )
                }
            }
        else ->
            result.result()
    }

    this.response()
        .putHeader("content-type", "application/json")
        .setStatusCode(200)
        .end(result.let { response.toJson().encode() })
}

