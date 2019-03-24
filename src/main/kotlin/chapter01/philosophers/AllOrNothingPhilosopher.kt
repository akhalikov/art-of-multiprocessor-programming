package chapter01.philosophers

import utils.waitMillis

/**
 * Only eat if you can pick up both sticks
 */
class AllOrNothingPhilosopher(id: Int,
                              left: Chopstick,
                              right: Chopstick): AbstractPhilosopher(id, left, right) {

  override fun eat() {
    if (!pickUpChopsticks()) {
      log("waiting...")
    } else {
      eatCounter++
      log("eating with $left and $right (#$eatCounter)")
      waitMillis(EAT_TIME_MILLIS)
      putDownChopsticks()
    }
  }

  override fun pickUpChopsticks(): Boolean {
    if (!left.pickUp()) {
      return false
    }
    if (!right.pickUp()) {
      return false
    }
    return true
  }
}