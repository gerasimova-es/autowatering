package com.home.autowatering

import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.ext.web.Router


class Application : AbstractVerticle() {

    override fun start(startFuture: Future<Void>) {
        val server = vertx.createHttpServer()
        val router = Router.router(vertx)
        router.get("/list").handler { routingContext ->
            // This handler will be called for every request
            val response = routingContext.response()
            response.putHeader("content-type", "text/plain")
            // Write to the response and end it
            response.end("Hello World from Vert.x-Web!")
        }
        server.requestHandler(router).listen(8080)
    }
}
