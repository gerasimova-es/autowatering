package com.home.autowatering

import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.eventbus.EventBus
import io.vertx.core.json.JsonObject
import io.vertx.ext.auth.AuthProvider
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.JWTAuthHandler


//https://www.mednikov.net/case-studies/an-easy-introduction-to-microservices-with-vertx-and-java/
class Application : AbstractVerticle() {
    private lateinit var eventBus: EventBus
    private lateinit var authProvider: AuthProvider

    override fun start(startFuture: Future<Void>) {
        super.start(startFuture)
        val initSteps = initAuthProvider().compose({ verticle -> initServer() })
        initSteps.setHandler(startFuture.completer())
    }

    fun initServer(): Future<Void> {
        val future = Future.future<Void>()
        //initialize web server and router
        val server = vertx.createHttpServer()
        val router = Router.router(vertx)

        //Don't forget to get an eventbus reference!
        this.eventBus = vertx.eventBus()


        //secure routes with AuthProvider
        router.route("/secure/*").handler(JWTAuthHandler.create(authProvider))
        //assign hanlder to routes
        router.get("/secure/chat/:id").handler { this.getChats() }

        //assign server
        server.requestHandler(router).listen(4567) { res ->
            if (res.succeeded()) {
                println("Created a server on port 4567")
            } else {
                System.out.println("Unable to create server: \n" + res.cause().localizedMessage)
                future.fail(res.cause())
            }
        }
        return future
    }

    private fun getChats(ctx: RoutingContext) {
        val chatId = ctx.pathParam("id")
        val request = JsonObject()
        request.put("chatId", chatId)
        //send request to consumer
        eventBus.send("consumer.service.messages.getchat", request, { res ->
            if (res.succeeded()) {
                ctx.response()
                    .setStatusCode(200)
                    .end(Json.encodePrettily(res.result().body()))
            } else {
                System.out.println("Unable to get a chat:\n" + res.cause().getLocalizedMessage())
                ctx.response().setStatusCode(404).end()
            }
        })
    }
}
