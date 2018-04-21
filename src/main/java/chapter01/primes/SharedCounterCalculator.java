package chapter01.primes;

import static chapter01.primes.PrimeHelper.isPrime;
import static java.lang.System.currentTimeMillis;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;
import static utils.ThreadUtils.joinThreads;
import static utils.TimeUtils.stopWatchSec;

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
public class SharedCounterCalculator {
  private static final int UPPER_LIMIT = (int) Math.pow(10, 7);
  private static final int[] PRIMES_COUNT_BY_THREAD = new int[10];
  private static final AtomicLong PRIMES_COUNTER = new AtomicLong(0);

  private static boolean debug = false;
  private static final List<Integer> PRIMES = new ArrayList<>();

  public static void main(String[] args) {
    System.out.println("Calculating primes for max=" + UPPER_LIMIT);

    long start = currentTimeMillis();

    var counter = new AtomicLong(0);
    var threads = new Thread[10];
    for (int id = 0; id < threads.length; id++) {
      threads[id] = new Thread(new PrimeCalculator(id, counter));
      threads[id].start();
    }

    joinThreads(threads);

    var stop = stopWatchSec(start);

    assert IntStream.of(PRIMES_COUNT_BY_THREAD).sum() == PRIMES_COUNTER.get();
    for (int id = 0; id < PRIMES_COUNT_BY_THREAD.length; id++) {
      System.out.println("thread " + id + ": primes=" + PRIMES_COUNT_BY_THREAD[id]);
    }

    System.out.println();
    System.out.println("finished in " + stop + " sec.");
    System.out.println("total number of primes=" + PRIMES_COUNTER.get());

    printDebugInfo();
  }

  static class PrimeCalculator implements Runnable {
    private final int threadId;
    private final AtomicLong counter;

    PrimeCalculator(int threadId, AtomicLong sharedCounter) {
      this.threadId = threadId;
      this.counter = sharedCounter;
    }

    @Override
    public void run() {
      // todo
      // There is some problem to be inspected here.
      // Sometimes the total number of primes slightly differs from the expected number.
      // I compare it with the results of InputBatchCalculator that works stable.

      long number = counter.get();
      while (number < UPPER_LIMIT) {
        if (isPrime(number)) {
          PRIMES_COUNT_BY_THREAD[threadId]++;
          PRIMES_COUNTER.incrementAndGet();
          if (debug) {
            PRIMES.add((int) number);
          }
        }
        number = counter.getAndIncrement();
      }
    }
  }

  private static void printDebugInfo() {
    if (!debug) {
      return;
    }

    var primes = PRIMES.stream().mapToInt(Integer::intValue).sorted().toArray();

    for (int i = 1; i < primes.length; i++) {
      if (primes[i] == primes[i-1]) {
        System.out.println("same numbers twice: " + primes[i]);
      }
    }
    System.out.println("last 10 numbers");
    for (int i = primes.length - 10; i < primes.length; i++) {
      System.out.println(primes[i]);
    }
  }
}
