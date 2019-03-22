package utils

fun joinThreads(threads: Array<Thread>) {
  try {
    for (t in threads) {
      t.join()
    }
  } catch (e: InterruptedException) {
    throw RuntimeException(e)
  }
}

fun stopWatchSec(start: Long): Double {
  return (System.currentTimeMillis() - start) / 1000.0
}
