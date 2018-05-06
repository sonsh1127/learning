package scalabook

import scala.collection.mutable

object Ch24Iterable {
  val xs = List(1, 2, 3, 4, 5)
  xs(3)

  // grouped iterator
  val git = xs.grouped(3)

  while (git.hasNext) {
    println(git.next())
  }

  val sit = xs sliding (3)
  while (sit.hasNext) {
    println(sit.next())
  }

  def f(x: String) = {
    println("taking my time.")
    Thread.sleep(100)
    x.reverse
  }

  val map = collection.mutable.Map[String, String]()

  val x = "abc"
  map.getOrElseUpdate(x, f(x))
  map(x)
  map(x)

  // stream creation
  val stream = 1 #:: 2 #:: Stream.empty
  println(stream)

  val fibs = fibFrom(1, 1).take(33)
  println(fibs.toList)

  // more complicated case
  def fibFrom(a: Int, b: Int): Stream[Int] =
    a #:: fibFrom(b, a + b)

  //
  val a1 = Array(1, 2, 3)
  val seq1: Seq[Int] = a1
  println(seq1.getClass)

  // collection equality
  assert(List(1, 2, 3) == Vector(1, 2, 3))
  assert(List(1, 2, 3) != Set(1, 2, 3))

  // view
  def lazyMap[T, U](coll: Iterable[T], f: T => U) = {
    new Iterable[U] {
      override def iterator: Iterator[U] = coll.iterator.map(f)
    }
  }

  def main(args: Array[String]): Unit = {
    val v = Vector(1 to 10: _*)
    val res = v map (_ + 1) map (_ * 2)
    println(res)

    val res2 = (v.view map (_ + 1) map (_ * 2)).force
    println(res2)

    val vv = v.view
    println(vv)

    val mV = vv map (_ + 1)
    println(mV)

    //  intermediate data structure is not necessary
    val mmV = mV map (_ * 2)
    println(mmV)
    println(mmV.force)

    // First reason for using view : performance
    val seq = Seq("", "", "")

    def isPalindrome(x: String) = x == x.reverse
    def findPalindrome(s: Seq[String]) = s find isPalindrome

    findPalindrome(seq take 1000000)

    findPalindrome(seq.view take 1000000)

    // Second reason for using view : selectable window about mutable sequence
    def negate(xs: mutable.Seq[Int]) =
      for (i <- 0 until xs.length) xs(i) = -xs(i)

    val arr = (0 to 9).toArray
    val subArr = arr.view.slice(3, 6)
    negate(subArr)

    // sequence factory methods
    val emptySeq = Seq.empty
    val xyz = Seq("x", "Y","Z")
    val fill = Seq.fill(10)(1)

    val twoDim = Seq.fill(2, 3)(2)

  }

}
