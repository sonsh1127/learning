package scalabook


object Test {
  //operator precedence
  def main(args: Array[String]): Unit = {

    val x = new Rational(1, 2)
    val y = new Rational(2, 3)
    // operator precedence sample * > +
    println(x + x * y)
    println((x + x) * y)
    println(x + (x * y))

    println(x + 2)

    println(x * 2)

    implicit def intToRational(i : Int)  = new Rational(i)
    println(2 * x)

  }

}


class Rational(n: Int, d: Int) {
  require(d != 0)
  private val g = gcd(n.abs, d.abs)
  val numer: Int = n / g

  val denom: Int = d / g

  override def toString = n + "/" + d

  def this(n: Int) = this(n, 1)

  def +(that: Rational): Rational =
    new Rational(
      numer * that.denom + denom * that.numer,
      denom * that.denom
    )

  def +(i: Int): Rational = new Rational(numer + denom * i, denom)

  def -(that: Rational): Rational =
    new Rational(
      numer * that.denom - denom * that.numer,
      denom * that.denom
    )

  def - (i : Int): Rational = new Rational(numer - denom * i, denom)

  def *(that: Rational): Rational =
    new Rational(numer * that.numer, denom * that.denom)

  def *(i : Int): Rational = new Rational(numer * i, denom)

  private def gcd(a: Int, b: Int): Int = {
    if (b == 0) a else gcd(b, a % b)
  }
}