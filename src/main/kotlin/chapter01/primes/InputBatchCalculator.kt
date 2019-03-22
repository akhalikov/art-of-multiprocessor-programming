package chapter01.primes

import chapter01.primes.PrimeHelper.isPrime
import utils.joinThreads
import utils.stopWatchSec
import java.lang.System.currentTimeMillis
import java.util.concurrent.atomic.AtomicLong

private const val MAX_POWER_OF_TEN = 5
private val BATCH_SIZE = Math.pow(10.0, (MAX_POWER_OF_TEN - 1).toDouble()).toInt()
private val PRIMES_COUNTER = AtomicLong(0)

/**
 * Balancing load by dividing up the input domain.
 * Each thread in {0..9} gets and equal subset of the range (batch).
 *
 * VM options: -Xmx64M
 *
 * Example statistics for MAX = 10^7:
 *
 * thread 0 finished in 5.141 sec. primes = 78499
 * thread 1 finished in 7.924 sec. primes = 70435
 * thread 2 finished in 9.164 sec. primes = 67883
 * thread 3 finished in 10.299 sec. primes = 66330
 * thread 4 finished in 10.682 sec. primes = 65367
 * thread 5 finished in 11.04 sec. primes = 64336
 * thread 6 finished in 11.333 sec. primes = 63799
 * thread 7 finished in 11.605 sec. primes = 63129
 * thread 8 finished in 11.742 sec. primes = 62712
 * thread 9 finished in 11.862 sec. primes = 62090
 *
 * total time: 12.103 sec.
 * total number of primes=664580
 *
 */
object InputBatchCalculator {

  @JvmStatic
  fun main(args: Array<String>) {
    println("Calculating primes for max=" + Math.pow(10.0, MAX_POWER_OF_TEN.toDouble()))
    val start = currentTimeMillis()

    val threads = Array(10) {
      Thread(PrimeCalculator(it))
    }

    threads.forEach { it.start() }

    joinThreads(threads)

    println()
    println("finished in: " + stopWatchSec(start) + " sec.")
    println("total number of primes=" + PRIMES_COUNTER.get())
  }

  private class PrimeCalculator(private val threadId: Int) : Runnable {
    override fun run() {
      val start = currentTimeMillis()
      var count: Long = 0
      for (j in threadId * BATCH_SIZE + 1..(threadId + 1) * BATCH_SIZE) {
        if (isPrime(j.toLong())) {
          count++
          PRIMES_COUNTER.incrementAndGet()
        }
      }
      println("thread " + threadId + " finished in " + stopWatchSec(start) + " sec. primes = " + count)
    }
  }
}
