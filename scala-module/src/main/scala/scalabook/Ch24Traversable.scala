package scalabook

object Ch24Traversable extends App {

  val list = List(1, 2, 3, 4)
  val xs = List(5,6,7,8)
  val ys = List(9, 10, 11, 12)

  xs.foreach(println)
  println(xs.getClass)
  val array = xs.toArray

}
