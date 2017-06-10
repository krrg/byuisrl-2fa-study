package edu.byu.isrl.routers

import java.util

import io.vertx.core.http.HttpMethod
import io.vertx.core.json.JsonObject
import io.vertx.lang.scala.json.{Json, JsonObject}
import io.vertx.scala.core.Vertx
import io.vertx.scala.ext.web.Router


class UsernamePassword(vertx: Vertx) {

  var router = Router.router(vertx)

  router.route(HttpMethod.GET, "/").handler((context) => {
    var response = context.response()

    response.headers().add("Content-Type", "application/json")
    response.end(Json.obj(("alive", true)).encodePrettily())

  })

}

object UsernamePassword {

  def apply(vertx: Vertx): Router = new UsernamePassword(vertx).router

}

