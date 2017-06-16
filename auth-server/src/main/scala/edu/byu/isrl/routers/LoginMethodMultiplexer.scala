package edu.byu.isrl.routers

import io.vertx.core.http.HttpMethod
import io.vertx.scala.core.Vertx
import io.vertx.scala.ext.web.Router

import scala.concurrent.ExecutionContext

/**
  * Created by krr428 on 6/16/17.
  */
class LoginMethodMultiplexer(vertx: Vertx)(implicit executionContext: ExecutionContext) {

  val router = Router.router(vertx)


  router.mountSubRouter("/accounts/login", {
    val subRouter = Router.router(vertx)

    subRouter.route(HttpMethod.POST, "/authenticator-app")



    subRouter
  })

}


