package edu.byu.isrl.models

import java.nio.charset.StandardCharsets

import io.vertx.lang.scala.json.Json
import io.vertx.scala.core.Vertx
import org.abstractj.kalium.NaCl

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by krr428 on 6/10/17.
  */
class Accounts(vertx: Vertx) {

  private lazy val couchClient = CouchDBClient(vertx)

  def exists(username: String): Future[Boolean] = {

    couchClient.get("/accounts/_find").sendJsonFuture(
      Json.emptyObj()
        .put("selector", Json.obj(("username", username)))
        .put("limit", 1)
        .put("fields", Json.arr("_id"))
    )
    .map { httpResponse =>
      httpResponse.statusCode() match {
        case 200 => true
        case _ => false
      }
    }

  }

//  def create(username: String, password: String): Future[UUID] = {
//
//  }

}

object PasswordUtils {

  NaCl.init()

  private val INPUT_CHARSET = StandardCharsets.UTF_16
  private val HASH_CHARSET = StandardCharsets.US_ASCII

  def hashAndSaltPassword(password: String): String = {

    /*
      The lack of quality Java libraries for doing proper password hashing is astounding.
      Kalium is okay, but doesn't support Argon2, even though libsodium does.  <sigh>

      And it isn't totally obvious what the sane defaults ought to be at first.
      It is no wonder normal people can't do this correctly.

      The code below represents a best-effort.
     */

    val outputBytes: Array[Byte] = new Array(NaCl.Sodium.CRYPTO_PWHASH_SCRYPTSALSA208SHA256_STRBYTES)
    val inputBytes: Array[Byte] = password.getBytes(INPUT_CHARSET)

    /*
    Quote from documentation:

    crypto_pwhash_scryptsalsa208sha256_OPSLIMIT_INTERACTIVE and crypto_pwhash_scryptsalsa208sha256_MEMLIMIT_INTERACTIVE
     are safe baseline values to use for opslimit and memlimit
    */

    NaCl.sodium().crypto_pwhash_scryptsalsa208sha256_str(
      outputBytes, inputBytes, inputBytes.length,
      NaCl.Sodium.CRYPTO_PWHASH_SCRYPTSALSA208SHA256_OPSLIMIT_INTERACTIVE,
      NaCl.Sodium.CRYPTO_PWHASH_SCRYPTSALSA208SHA256_MEMLIMIT_INTERACTIVE
    )

    /* The output bytes are actually in ASCII format according to docs */
    new String(outputBytes, HASH_CHARSET)
  }


  def verifyPassword(password: String, hashSaltedPassword: String): Boolean = {

    val hashBytes = hashSaltedPassword.getBytes(HASH_CHARSET) /* This may not be zero terminated?  Not sure... */
    val passwordBytes = password.getBytes(INPUT_CHARSET)

    val weirdResult = NaCl.sodium().crypto_pwhash_scryptsalsa208sha256_str_verify(
      hashBytes, passwordBytes, passwordBytes.length
    )

    /* These numbers probably make sense in a Unix sense, but could be misinterpreted very easily */
    weirdResult match {
      case -1 => false
      case 0 => true  /* So 0 == true, huh?  Kind of... */
    }

  }


}
