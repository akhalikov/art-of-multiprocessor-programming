package utils;

public class ThreadUtils {
  public static void joinThreads(Thread[] threads) {
    try {
      for (Thread t : threads) {
        t.join();
      }
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
