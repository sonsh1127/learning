package scalabook

class ScalaControl {
  val array = Array(1, 2, 10, 4)
  def test(i: Int): Boolean = {
    for (j <- 0 to i) {
      if (array(i) < 10) {
        return false
      }
    }
    true
  }
}


object Main {
  def main(args: Array[String]): Unit = {
    val control = new ScalaControl
    println(control.test(1))
    println(control.test(2))
    println(control.test(2))
  }
}
