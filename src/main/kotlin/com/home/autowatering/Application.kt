package com.home.autowatering

import com.home.autowatering.controller.PotController
import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.json.Json
import io.vertx.ext.web.Router

//https://www.mednikov.net/case-studies/an-easy-introduction-to-microservices-with-vertx-and-java/
class Application(
    private val potController: PotController = PotController()
) : AbstractVerticle() {

    override fun start(future: Future<Void>) {
        super.start(future)
        val initSteps = initServer()
        initSteps.setHandler(future)
    }

    private fun initServer(): Future<Void> {
        val server = vertx.createHttpServer()
        val router = Router.router(vertx)

        router.route("/pot/list").handler { context ->
            val response = potController.list()
            context.response()
                .putHeader("content-type", "application/json")
                .setStatusCode(200)
                .end(Json.encodePrettily(response))
        }

        with(Future.future<Void>()) {
            //assign server
            server.requestHandler(router)
                .listen(config().getInteger("http.port", 8080)) { res ->
                    if (res.succeeded()) {
                        println("Created a server on port 8080")
                    } else {
                        println("Unable to create server: \n" + res.cause().localizedMessage)
                        this.fail(res.cause())
                    }
                }
            return this
        }

    }

}
