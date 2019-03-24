package chapter01.philosophers

import utils.awaitTermination

fun main() {

  val philosophers = PhisilopherType.ALL_OR_NOTHING.createSome(5)

  val threads = arrayOfNulls<Thread>(philosophers.size)
  for (i in threads.indices) {
    val t = Thread(philosophers[i])
    t.start()
    threads[i] = t
  }

  awaitTermination(threads)

  for (p in philosophers) {
    println(p.getResult())
  }
}
