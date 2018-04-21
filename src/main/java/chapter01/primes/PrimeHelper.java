package chapter01.primes;

class PrimeHelper {

  static boolean isPrime(long num) {
    if (num == 0 || num == 1) {
      return false;
    } else {
      for (int i = 2; i * i <= num; i++) {
        if (num % i == 0) {
          return false;
        }
      }
      return true;
    }
  }
}
