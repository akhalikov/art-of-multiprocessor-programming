package chapter01.primes;

import static chapter01.primes.PrimeHelper.isPrime;
import static java.lang.System.currentTimeMillis;
import java.util.concurrent.atomic.AtomicLong;
import static utils.ThreadUtils.joinThreads;
import static utils.TimeUtils.stopWatchSec;

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
public class InputBatchCalculator {
  private static final int MAX_POWER_OF_TEN = 5;
  private static final int BATCH_SIZE = (int) Math.pow(10, MAX_POWER_OF_TEN - 1);
  private static final AtomicLong PRIMES_COUNTER = new AtomicLong(0);

  public static void main(String[] args) {
    System.out.println("Calculating primes for max=" + Math.pow(10, MAX_POWER_OF_TEN));
    long start = currentTimeMillis();

    var threads = new Thread[10]; // type inference

    for (int i = 0; i < threads.length; i++) {
      threads[i] = new Thread(new PrimeCalculator(i));
      threads[i].start();
    }

    joinThreads(threads);

    System.out.println();
    System.out.println("finished in: " + stopWatchSec(start) + " sec.");
    System.out.println("total number of primes=" + PRIMES_COUNTER.get());
  }

  static class PrimeCalculator implements Runnable {
    private final int threadId;

    PrimeCalculator(int threadId) {
      this.threadId = threadId;
    }

    @Override
    public void run() {
      long start = currentTimeMillis();
      long count = 0;
      for (int j = (threadId * BATCH_SIZE) + 1; j <= (threadId + 1) * BATCH_SIZE; j++) {
        if (isPrime(j)) {
          count++;
          PRIMES_COUNTER.incrementAndGet();
        }
      }
      System.out.println("thread " + threadId + " finished in " + stopWatchSec(start) + " sec. primes = " + count);
    }
  }
}
