package chapter01.primes

object PrimeHelper {

    fun isPrime(num: Long): Boolean {
      if (num == 0L || num == 1L) {
        return false
      } else {
        var i = 2
        while (i * i <= num) {
          if (num % i == 0L) {
            return false
          }
          i++
        }
        return true
      }
    }
}