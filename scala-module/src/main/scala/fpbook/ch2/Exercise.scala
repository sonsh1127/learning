package fpbook.ch2

object Exercise {
  def isSorted[A](arr: Array[A], ordered: (A, A) => Boolean): Boolean = {
    def loop(arr: Array[A], ordered: (A, A) => Boolean, i: Int): Boolean = {
      if (i == arr.length - 1) true
      else if (ordered(arr(i), arr(i + 1))) loop(arr, ordered, i + 1)
      else false
    }

    loop(arr, ordered, 0)
  }


  def fibo(n: Int): Int = {
    def loop(n: Int, a: Int, b: Int): Int = {
      if (n <= 0) b
      else loop(n - 1, b, a + b)
    }

    loop(n, 0, 1)
  }


  def curry[A, B, C](f: (A, B) => C): A => (B => C) = {
    (a: A) => (b: B) => f(a, b)
  }

  def uncurry[A, B, C](f: A => (B => C)): (A, B) => C = {
    (a: A, b: B) => f(a)(b)
  }

  def compose[A, B, C](f: B => C, g: A => B): A => C = {
    (a: A) => f(g(a))
  }

}
