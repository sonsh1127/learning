package scalabook

case class MyClass11(val id: Int) {

  def nills() = {
    println("nills")
  }
}


object MyClassConverters {

  implicit def toMyClass(n: Int): MyClass11 = {
    MyClass11(n)
  }
}


