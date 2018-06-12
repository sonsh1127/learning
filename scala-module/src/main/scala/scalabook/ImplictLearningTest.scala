package scalabook

object ImplictLearningTest {

  implicit def doubleToInt(x: Double) = x.toInt


  def main(args: Array[String]): Unit = {


    //

    val x : Int = 3.5 // val x : Int = doubleToInt(3.5)


  }

}
