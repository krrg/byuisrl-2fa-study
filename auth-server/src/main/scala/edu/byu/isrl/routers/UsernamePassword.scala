package edu.byu.isrl.routers

import edu.byu.isrl.models.Accounts
import io.vertx.core.http.HttpMethod
import io.vertx.lang.scala.json.Json
import io.vertx.scala.core.Vertx
import io.vertx.scala.ext.web.Router

import scala.concurrent.ExecutionContext
import scala.util.Try


class UsernamePassword(vertx: Vertx)(implicit executionContext: ExecutionContext) {

  var router = Router.router(vertx)
  var accounts = Accounts(vertx)


  router.route(HttpMethod.GET, "/").handler((context) => {
    var response = context.response()

    response.headers().add("Content-Type", "application/json")
    response.end(Json.obj(("alive", true)).encodePrettily())

  })


  router.route(HttpMethod.POST, "/account").handler((context) => {
    context.request().bodyHandler(body => {

      val optJson = Try(body.toJsonObject).toOption

      optJson match {
        case None =>

          /* Then respond with a 400 */
          context.response().setStatusCode(400).end("Expected JSON with `username` and `password` field.")

        case Some(jsonBody) =>
          val username = jsonBody.getString("username")
          val password = jsonBody.getString("password")

          println(s"Creating account for ${username}")

          val futAccountCreated = accounts.create(username, password)

          futAccountCreated.map {
              case None => context.response().setStatusCode(409 /* Conflict */).end()
              case Some(userId) =>
                context.response().setStatusCode(201 /* Created */)
                context.response().putHeader("Content-Type", "application/json")
                context.response().end(Json.obj(("userId", userId)).encode())
            }

          futAccountCreated.failed.foreach { ex =>
            /* Note:  Do not return the actual stack trace to the user.  */
            println(ex)

            context.response().setStatusCode(500)
            context.response().end("Unfortunately, something went wrong on our end.")
          }


      }

    })

  })

}

object UsernamePassword {

  def apply(vertx: Vertx)(implicit executionContext: ExecutionContext): Router = {
    new UsernamePassword(vertx)(executionContext).router
  }

}

