package chapter01.primes

import java.util.concurrent.atomic.AtomicLong

import chapter01.primes.PrimeHelper.isPrime
import java.lang.System.currentTimeMillis
import utils.joinThreads
import utils.stopWatchSec

/**
 * Balancing the work load using a shared counter.
 * Each thread gets a dynamically determined number of numbers to test.
 *
 * Example statistics:
 *
 * Calculating primes for max=10000000
 * thread 0: primes=68426
 * thread 1: primes=67576
 * thread 2: primes=67432
 * thread 3: primes=66451
 * thread 4: primes=64929
 * thread 5: primes=66024
 * thread 6: primes=65409
 * thread 7: primes=65517
 * thread 8: primes=65371
 * thread 9: primes=67445
 *
 * finished in 9.15 sec.
 * total number of primes=664580
 */
object SharedCounterCalculator {
  private val UPPER_LIMIT = Math.pow(10.0, 7.0).toInt()
  private val PRIMES_COUNT_BY_THREAD = LongArray(10)
  private val PRIMES_COUNTER = AtomicLong(0)

  private const val debug = false
  private val PRIMES = ArrayList<Int>()

  @JvmStatic
  fun main(args: Array<String>) {
    println("Calculating primes for max=$UPPER_LIMIT")

    val start = currentTimeMillis()

    val counter = AtomicLong(0)
    val threads = Array(10) {
      Thread(PrimeCalculator(it, counter))
    }

    threads.forEach { t -> t.start() }

    joinThreads(threads)

    val stop = stopWatchSec(start)

    assert(PRIMES_COUNT_BY_THREAD.sum() == PRIMES_COUNTER.get())

    for (id in PRIMES_COUNT_BY_THREAD.indices) {
      println("thread " + id + ": primes=" + PRIMES_COUNT_BY_THREAD[id])
    }

    println()
    println("finished in $stop sec.")
    println("total number of primes=" + PRIMES_COUNTER.get())

    printDebugInfo()
  }

  private class PrimeCalculator(private val threadId: Int,
                                private val counter: AtomicLong) : Runnable {
    override fun run() {
      // There is some problem to be inspected here.
      // Sometimes the total number of primes slightly differs from the expected number.
      // I compare it with the results of InputBatchCalculator that works stable.

      var number = counter.get()
      while (number < UPPER_LIMIT) {
        if (isPrime(number)) {
          PRIMES_COUNT_BY_THREAD[threadId]++
          PRIMES_COUNTER.incrementAndGet()
          if (debug) {
            PRIMES.add(number.toInt())
          }
        }
        number = counter.getAndIncrement()
      }
    }
  }

  private fun printDebugInfo() {
    if (!debug) {
      return
    }

    val primes = PRIMES.stream().mapToInt { it.toInt() }.sorted().toArray()

    for (i in 1 until primes.size) {
      if (primes[i] == primes[i - 1]) {
        println("same numbers twice: " + primes[i])
      }
    }
    println("last 10 numbers")
    for (i in primes.size - 10 until primes.size) {
      println(primes[i])
    }
  }
}
