package chapter01.philosophers

import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

class Chopstick(private val id: Int) {
  private val lock: Lock = ReentrantLock()

  fun pickUp(): Boolean {
    lock.lock()
    return true
  }

  internal fun putDown() {
    lock.unlock()
  }

  override fun toString(): String {
    return "Chopstick-$id"
  }
}
