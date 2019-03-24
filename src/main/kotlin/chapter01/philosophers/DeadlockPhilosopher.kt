package chapter01.philosophers

import utils.waitMillis

/**
 * Deadlock can be observed in some "unhappy" runs
 */
class DeadlockPhilosopher(id: Int,
                          left: Chopstick,
                          right: Chopstick) : AbstractPhilosopher(id, left, right) {

  override fun eat() {
    pickUpChopsticks()
    eatCounter++
    log("eating with $left and $right (#$eatCounter)")
    waitMillis(EAT_TIME_MILLIS)
    putDownChopsticks()
  }

  override fun pickUpChopsticks(): Boolean {
    left.pickUp()
    right.pickUp()
    return true
  }
}
