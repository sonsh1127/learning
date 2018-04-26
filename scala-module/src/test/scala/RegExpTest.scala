object RegExpTest extends App{

  def containtsScala(x: String): Boolean = {
    val z: Seq[Char] = x
    z match {
      case Seq('s', 'c', 'a', 'l' , 'a', rest @ _) => println(rest); true
      case Seq(_*) => false
    }
  }

  def nills(n : String*) : Unit = {
    println(n)
  }

  println(containtsScala("scalaaa"))
  nills("11")
  nills("11", "22")
  nills("11", "33")
  val x = Twice(21)
  x match { case Twice(n) => Console.println(n) } // prints 21


}


object Twice {

  def apply(n: Int): Int = {
    println(n)
    n * 2
  };

  def unapply(z: Int): Option[Int] = if (z%2 == 0) Some(z*2) else None

}
