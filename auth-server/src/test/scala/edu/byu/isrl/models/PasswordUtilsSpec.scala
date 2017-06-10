package edu.byu.isrl.models

import org.scalatest.FlatSpec

/**
  * Created by krr428 on 6/10/17.
  */
class PasswordUtilsSpec extends FlatSpec {

  it should "be able to verify a simple password" in {

    val password = "an example password"
    val hashed = PasswordUtils.hashAndSaltPassword(password)

    assert(PasswordUtils.verifyPassword(password, hashed))

    println(hashed)
  }

  it should "fail a password that does not match" in {

    val password = "a correct donkey rayovac nail"
    val incorrectPassword = "an incorrect bag of leather"

    val hashed = PasswordUtils.hashAndSaltPassword(password)

    assert(! PasswordUtils.verifyPassword(incorrectPassword, hashed))

    println(hashed)
  }

}
