package scalabook

object Ch16 {
  def main(args: Array[String]): Unit = {
   val List(a, b, c) = List(1,2,3)
    println(a)
    println("nins")

    println(1 :: List(1,2,3))
    println(append(List(1,2,3), List(4,5,6)))
    assert(List(1,2,3,4,5,6) == append(List(1,2,3), List(4,5,6)))

    val numbers = List(1,2,3,4,5,6)
    val head = numbers.head
    println(head)
    val tail = numbers.tail
    println(tail)
    val init = numbers.init
    println(init)
    val last = numbers.last
    println(last)
  }

  def append[T](xs: List[T], ys: List[T]): List[T] = {
    xs match {
      case List() => ys
      case _ => xs.head :: append(xs.tail, ys)
    }
  }

  def msort[T](less: (T, T) => Boolean) (xs: List[T]): List[T] = {
    def merge(xs: List[T], ys: List[T]) : List[T] = {
      (xs, ys) match {
        case (Nil, _) => ys
        case(_, Nil) => xs
        case (x :: xs1, y :: ys1) =>
          if (less(x, y)) x :: merge(xs1, x :: ys)
          else y :: merge(xs, ys1)
      }
    }

    val n = xs.length / 2
    if (n == 0) xs
    else {
      val (ys, zs) = xs.splitAt(n)
      merge(msort(less)(ys), msort(less)(zs))
    }
  }
}
