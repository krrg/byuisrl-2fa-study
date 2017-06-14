package edu.byu.isrl

import edu.byu.isrl.routers.UsernamePassword
import io.vertx.lang.scala.ScalaVerticle
import io.vertx.scala.core.Vertx
import io.vertx.scala.ext.web.Router

import scala.concurrent.Future

/**
  * Created by krr428 on 6/9/17.
  */
object Main {

  def main(args: Array[String]): Unit = {
    new MainHttpVerticle().startFuture()
  }

}

class MainHttpVerticle extends ScalaVerticle {

  override def startFuture(): Future[Unit] = {
    val vertx = Vertx.vertx()

    val rootRouter = Router.router(vertx)
    rootRouter.mountSubRouter("/", UsernamePassword(vertx))

    vertx.createHttpServer()
      .requestHandler(rootRouter.accept _)
      .listenFuture(7000)
      .map(server => println(s"Starting the server on port ${server.actualPort()}"))


  }

}
