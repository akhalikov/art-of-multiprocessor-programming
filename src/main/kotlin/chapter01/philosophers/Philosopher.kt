package chapter01.philosophers

import utils.waitMillis

const val THINK_TIME_MILLIS = 2
const val EAT_TIME_MILLIS = 2
const val DEBUG = false

interface Philosopher: Runnable {

  fun eat()
  fun pickUpChopsticks(): Boolean
  fun putDownChopsticks()
  fun getResult(): String

  fun log(msg: String) {
    if (DEBUG) {
      println("$this: $msg")
    }
  }

  fun think() {
    log("thinking...\r\n")
    waitMillis(THINK_TIME_MILLIS)
  }
}

abstract class AbstractPhilosopher(
    private val id: Int,
    val left: Chopstick,
    val right: Chopstick) : Philosopher {

  var eatCounter: Int = 0

  companion object {
    const val NUM_EATS: Int = 10_000
  }

  override fun run() {
    for (i in 0 until NUM_EATS) {
      eat()
      think()
    }
  }

  override fun putDownChopsticks() {
    right.putDown()
    left.putDown()
  }

  override fun toString(): String {
    return "Philosopher-$id"
  }

  override fun getResult(): String {
    var msg = "done"
    if (eatCounter == 0) {
      msg = "left hungry"
    } else if (eatCounter < NUM_EATS) {
      val eatsLeft = NUM_EATS - eatCounter
      msg = "$eatsLeft eats left"
    }
    return "$this: $msg"
  }
}
