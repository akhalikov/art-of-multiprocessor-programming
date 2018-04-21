package utils;

import static java.lang.System.currentTimeMillis;

public class TimeUtils {

  public static double stopWatchSec(long start) {
    return (currentTimeMillis() - start) / 1000.0;
  }
}
