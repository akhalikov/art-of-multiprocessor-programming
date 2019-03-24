package chapter01.philosophers

enum class PhisilopherType {
  DEADLOCK,
  ALL_OR_NOTHING;

  fun createSome(num: Int): Array<Philosopher> {

    val chopsticks = Array(num) { i -> Chopstick(i+1) }

    return Array(num) { index ->
      val left = if (index + 1 >= num) 0 else index + 1
      if (DEADLOCK == this) {
        DeadlockPhilosopher(index + 1, chopsticks[left], chopsticks[index])
      } else {
        AllOrNothingPhilosopher(index + 1, chopsticks[left], chopsticks[index])
      }
    }
  }
}
