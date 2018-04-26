object ObjectLearningTest extends App{

  val x = List(23,23231)

  println(Blah.sum(x))
}


object Blah {
  def sum(l: List[Int]): Int = l.sum
}