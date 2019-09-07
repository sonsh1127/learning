package scalabook

import scala.collection.immutable
import scala.collection.immutable.Stack

object ImplictLearningTest {


  implicit def doubleToInt(x: Double) = x.toInt

  def main(args: Array[String]): Unit = {
    //
    val x: Int = 3.5 // val x : Int = doubleToInt(3.5)

    val xs = List(1, 2, 3)

    val n = for (
      x <- xs
    ) yield ()
    println(n)

    import MyClassConverters._
    1.nills()

  }

}

