package edu.byu.isrl

import edu.byu.isrl.routers.Accounts
import io.vertx.lang.scala.ScalaVerticle
import io.vertx.scala.core.Vertx
import io.vertx.scala.ext.web.Router

import scala.concurrent.Future


object Main {

  def main(args: Array[String]): Unit = {
    val vertx: Vertx = Vertx.vertx()
    vertx.deployVerticle(s"scala:${classOf[MainHttpVerticle].getName}")
  }

}

class MainHttpVerticle extends ScalaVerticle {

  override def startFuture(): Future[Unit] = {

    println(s"We are using the execution context of ${executionContext}")

    val rootRouter = Router.router(vertx)
    rootRouter.mountSubRouter("/", Accounts(vertx))

    vertx.createHttpServer()
      .requestHandler(rootRouter.accept _)
      .listenFuture(7000)
      .map(server => println(s"Starting the server on port ${server.actualPort()}"))

  }

}

