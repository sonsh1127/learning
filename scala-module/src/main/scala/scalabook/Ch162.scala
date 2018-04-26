package scalabook

object Ch162 {
  def main(args: Array[String]): Unit = {
    val list = List(1, 2, 3, 4)
    val addOne = list.map(_ + 1)
    println(addOne)
    val s = "hello"
    val li = s.toList.reverse
    println(li)
    println(List.range(1, 5))
    println(List.range(1, 5).flatMap(i => List.range(1, i) map (j => (i, j))))

    filterFunctions()
    fold()
  }

  def filterFunctions() {
    val ints = List(1, 2, 3, 4, 5)
    println(ints.filter(_ % 2 == 0))
    println(ints.partition(_ % 2 == 0))
    println(ints.find(_ % 2 == 0))
    println(ints.find(_ % 2 == 3))
    println(ints.takeWhile(_ < 3))
    println(ints.dropWhile(_ < 3))
    println(ints.span(_ < 3))
    println(ints.drop(2))
    println(ints.take(2))

    println(reverse1(ints))

    val rev = reverse1(ints)
    val ordered = rev.sortWith(_ < _)
    println(ordered)


    val numbers2 = List.range(1, 5)
  }

  def fold() {
    val words : List[String] = List("the", "quick")
    println( ("" /: words){_ + " " + _} )
  }

  def sum(xs: List[Int]): Int = (0 /: xs) {
    _ + _
  }

  def product(xs: List[Int]): Int = (1 /: xs) {
    _ * _
  }

  // inefficient implementation
  def rev[T](xs: List[T]): List[T] = {
    xs match {
      case List() => Nil
      case x :: xs1 => rev(xs1) ::: List(x)
    }
  }

  def reverse1[T](xs: List[T]): List[T] = {
    xs.foldLeft(List[T]()){
      (ys, y) => y :: ys
    }
  }


}
