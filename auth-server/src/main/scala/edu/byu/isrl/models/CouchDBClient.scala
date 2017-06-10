package edu.byu.isrl.models

import edu.byu.isrl.models
import io.vertx.scala.core.Vertx
import io.vertx.scala.ext.web.client.{WebClient, WebClientOptions}

/**
  * Created by krr428 on 6/10/17.
  */
class CouchDBClient(vertx: Vertx, config: CouchDBClientConfiguration) {

  lazy val client: WebClient = {
    WebClient.create(vertx, WebClientOptions()
        .setMaxPoolSize(config.poolSize)
        .setDefaultHost(config.hostname)
        .setDefaultPort(config.port)
    )
  }

}

object CouchDBClient {

  private var _defaultInstance: Option[WebClient] = None

  def defaultInstance: WebClient = {
    _defaultInstance.get
  }
  
  def defaultInstanceConfig(vertx: Vertx, config: CouchDBClientConfiguration): models.CouchDBClient.type = {
    this._defaultInstance = Some(CouchDBClient(vertx, config))
    this
  }

  def apply(vertx: Vertx, config: CouchDBClientConfiguration = CouchDBClientConfiguration()): WebClient = {
    new CouchDBClient(vertx, config).client
  }

}

class CouchDBClientConfiguration() {

  private var _hostname = "127.0.0.1"
  private var _port = 5984
  private var _poolSize = 1  /* Setting a very small pool size in dev can prevent accidental blocks */

  def hostname = _hostname
  def port = _port
  def poolSize = _poolSize

  def setHostname(hostname: String): CouchDBClientConfiguration = {
    this._hostname = hostname
    this
  }

  def setPort(port: Int): CouchDBClientConfiguration = {
    this._port = port
    this
  }

  def setPoolSize(poolSize: Int): CouchDBClientConfiguration = {
    this._poolSize = poolSize
    this
  }

}

object CouchDBClientConfiguration {
  def apply(): CouchDBClientConfiguration = new CouchDBClientConfiguration()
}
