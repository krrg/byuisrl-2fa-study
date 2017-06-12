package edu.byu.isrl

import edu.byu.isrl.routers.UsernamePassword
import io.vertx.scala.core.Vertx
import io.vertx.scala.ext.web.Router

/**
  * Created by krr428 on 6/9/17.
  */
object Main {

  def main(args: Array[String]): Unit = {

    var vertx = Vertx.vertx()
    var rootRouter = Router.router(vertx)
    rootRouter.mountSubRouter("/", UsernamePassword(vertx))

    var server = vertx.createHttpServer()
      .requestHandler(rootRouter.accept _)
      .listen(7000)

    println(s"Listening on ${server.actualPort()}")
  }

}
